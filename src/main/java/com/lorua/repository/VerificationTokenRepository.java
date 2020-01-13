package com.lorua.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.lorua.domain.User;
import com.lorua.domain.VerificationToken;

public interface VerificationTokenRepository extends Neo4jRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);
	 
    VerificationToken findByUser(User user);
	
}
