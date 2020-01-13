package com.lorua.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.lorua.annotations.ValidPassword;

public class PasswordDTO {
	
	
	@NotNull
	@NotEmpty
	private String oldPassword;
	
	@NotNull
	@NotEmpty
	@ValidPassword
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

}
