package com.lorua.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.lorua.domain.User;

public interface UserRepository extends Neo4jRepository<User, Long> {
		
	User findByEmail(@Param("email") String email);
	
}	
