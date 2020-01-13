package com.lorua.service;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lorua.DTO.UserDTO;
import com.lorua.domain.PasswordResetToken;
import com.lorua.domain.User;
import com.lorua.domain.VerificationToken;
import com.lorua.exception.UserAlreadyExistsException;
import com.lorua.repository.PasswordTokenRepository;
import com.lorua.repository.UserRepository;
import com.lorua.repository.VerificationTokenRepository;

@Service
public class UserService implements IUserService {
	@Autowired
	private UserRepository repository;
	@Autowired 
	private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	
	@Transactional
	@Override
	public User registerNewUserAccount(UserDTO accountDto) throws UserAlreadyExistsException {

		if (emailExists(accountDto.getEmail())) {
			throw new UserAlreadyExistsException("There is an account with that email address:" + accountDto.getEmail());
		}
		User user = new User();
		user.setName(accountDto.getFirstName() + " " + accountDto.getLastName());
		user.setPassword(accountDto.getPassword());
		user.setEmail(accountDto.getEmail());
		user.setRoles(Arrays.asList("ROLE_USER"));
		return repository.save(user);
	}

	private boolean emailExists(String email) {
		User user = repository.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

		@Override
	    public User getUserFromVerificationToken(String verificationToken) {
	        User user = tokenRepository.findByToken(verificationToken).getUser();
	        return user;
	    }
	     
	    @Override
	    public VerificationToken getVerificationToken(String VerificationToken) {
	        return tokenRepository.findByToken(VerificationToken);
	    }
	     
	    @Override
	    public void saveRegisteredUser(User user) {
	        repository.save(user);
	    }
	     
	    @Override
	    public void createVerificationToken(User user, String token) {
	        VerificationToken myToken = new VerificationToken(token, user);
	        tokenRepository.save(myToken);
	    }
	    

	    @Override
	    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
	        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
	        vToken.updateToken(UUID.randomUUID()
	            .toString());
	        vToken = tokenRepository.save(vToken);
	        return vToken;
	    }

		@Override
		public User findUserByEmail(String email) {
			return repository.findByEmail(email);
		}
		
		public void createPasswordResetTokenForUser(User user, String token) {
		    PasswordResetToken myToken = new PasswordResetToken(token, user);
		    passwordTokenRepository.save(myToken);
		}
		
		
		public void changeUserPassword(User user, String password) {
		    user.setPassword(passwordEncoder.encode(password));
		    repository.save(user);
		}
		
		public boolean checkIfValidOldPassword(User user, String oldPassword) {
			return user.getPassword().equals(oldPassword);
		}
		

}