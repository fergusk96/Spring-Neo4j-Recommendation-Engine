package com.lorua.service;

import com.lorua.DTO.UserDTO;
import com.lorua.domain.User;
import com.lorua.domain.VerificationToken;
import com.lorua.exception.UserAlreadyExistsException;

public interface IUserService {
    User registerNewUserAccount(UserDTO accountDto)     
      throws UserAlreadyExistsException;
    
    
    User findUserByEmail(String email);
    
    void saveRegisteredUser(User user);
 
    void createVerificationToken(User user, String token);
 
    VerificationToken getVerificationToken(String VerificationToken);

	VerificationToken generateNewVerificationToken(String existingVerificationToken);

	User getUserFromVerificationToken(String verificationToken);
}