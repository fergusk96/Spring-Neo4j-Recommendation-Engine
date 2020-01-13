package com.lorua.controller;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.lorua.DTO.PasswordDTO;
import com.lorua.DTO.UserDTO;
import com.lorua.domain.User;
import com.lorua.domain.VerificationToken;
import com.lorua.event.OnRegistrationCompleteEvent;
import com.lorua.exception.InvalidOldPasswordException;
import com.lorua.exception.UserAlreadyExistsException;
import com.lorua.exception.UserNotFoundException;
import com.lorua.exceptionhandler.GenericResponse;
import com.lorua.service.UserSecurityService;
import com.lorua.service.UserService;

public class RegistrationController {

	@Autowired
	UserService userService;

	@Autowired
	UserSecurityService securityService;

	@Autowired
	ApplicationEventPublisher applicationEventPulisher;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse registerUserAccount(@Valid UserDTO accountDto, HttpServletRequest request)
			throws UserAlreadyExistsException {
		User registered = createUserAccount(accountDto);
		if (registered == null) {
			throw new UserAlreadyExistsException();
		}
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		applicationEventPulisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));

		return new GenericResponse("success");
	}

	@RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse resendRegistrationToken(HttpServletRequest request,
			@RequestParam("token") String existingToken) {
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

		User user = userService.getUserFromVerificationToken(newToken.getToken());
		String appUrl = getAppUrl(request);
		SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, user);
		mailSender.send(email);

		return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
	}

	@RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

		Locale locale = request.getLocale();

		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = messages.getMessage("auth.message.invalidToken", null, locale);
			model.addAttribute("message", message);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
			model.addAttribute("expired", true);
			model.addAttribute("token", token);
			String messageValue = messages.getMessage("auth.message.expired", null, locale);
			model.addAttribute("message", messageValue);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
		}

		user.setEnabled(true);
		userService.saveRegisteredUser(user);
		return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
	}

	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail)
			throws UserNotFoundException {
		User user = userService.findUserByEmail(userEmail);
		if (user == null) {
			throw new UserNotFoundException();
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	}

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id,
			@RequestParam("token") String token) {
		String result = securityService.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
	}

	@RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse savePassword(Locale locale, @Valid PasswordDTO passwordDto) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		userService.changeUserPassword(user, passwordDto.getNewPassword());
		return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
	}

	@RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
	@PreAuthorize("hasRole('READ_PRIVILEGE')")
	@ResponseBody
	public GenericResponse changeUserPassword(Locale locale, @RequestParam("password") String password,
			@RequestParam("oldpassword") String oldPassword) throws InvalidOldPasswordException {
		User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!userService.checkIfValidOldPassword(user, oldPassword)) {
			throw new InvalidOldPasswordException();
		}
		userService.changeUserPassword(user, password);
		return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	private User createUserAccount(UserDTO accountDto) {
		User registered = null;
		try {
			registered = userService.registerNewUserAccount(accountDto);
		} catch (UserAlreadyExistsException e) {
			return null;
		}
		return registered;
	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		String message = messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("spring.mail.username"));
		return email;
	}

	private SimpleMailMessage constructResendVerificationTokenEmail(String contextPath, Locale locale,
			VerificationToken newToken, User user) {
		String confirmationUrl = contextPath + "/regitrationConfirm.html?token=" + newToken.getToken();
		String message = messages.getMessage("message.resendToken", null, locale);
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject("Resend Registration Token");
		email.setText(message + " rn" + confirmationUrl);
		email.setFrom(env.getProperty("support.email"));
		email.setTo(user.getEmail());
		return email;
	}
}
