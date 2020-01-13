package com.lorua.exception;

public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(String string) {
		super(string);
	}

}
