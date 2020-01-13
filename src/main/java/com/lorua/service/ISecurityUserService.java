package com.lorua.service;

public interface ISecurityUserService {

	String validatePasswordResetToken(long id, String token);

}
