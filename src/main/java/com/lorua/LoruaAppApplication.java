package com.lorua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication	
@EnableNeo4jRepositories("com.lorua.repositories")
public class LoruaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoruaAppApplication.class, args);
	}

}
