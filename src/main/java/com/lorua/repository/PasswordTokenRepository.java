package com.lorua.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.lorua.domain.PasswordResetToken;

public interface PasswordTokenRepository extends Neo4jRepository<PasswordResetToken, Long> {

	public PasswordResetToken findByToken(String token);
	
	
}
