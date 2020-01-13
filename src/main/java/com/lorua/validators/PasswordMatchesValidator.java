package com.lorua.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.lorua.DTO.UserDTO;
import com.lorua.annotations.PasswordMatches;

public class PasswordMatchesValidator 
implements ConstraintValidator<PasswordMatches, Object> { 
   
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {       
  }
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){   
      UserDTO user = (UserDTO) obj;
      return user.getPassword().equals(user.getMatchingPassword());    
  }     
}