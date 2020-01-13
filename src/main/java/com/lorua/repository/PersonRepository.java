package com.lorua.repository;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.lorua.domain.Person;

public interface PersonRepository extends Neo4jRepository<Person, Long> {
	
	Person findByName(@Param("name") String name);
	
	Collection<Person> findByNameLike(@Param("name") String name);
	
	@Query("MATCH (p2:Person)-[r:FRIEND_OF]-(p1:Person) RETURN p2,r,p1 LIMIT {limit}")
    Collection<Person> graph(@Param("limit") int limit);
	
	

}
