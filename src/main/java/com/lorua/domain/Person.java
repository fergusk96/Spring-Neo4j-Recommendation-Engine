package com.lorua.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Person {

    @Id
    @GeneratedValue
	private Long id;
	private String name;

	
	@Relationship(type = "FRIEND_OF", direction = Relationship.UNDIRECTED)
	private Set<Person> friendSet;
	
	@Relationship(type="IS_PERSON", direction = Relationship.INCOMING)
	private User user;
	

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Set<Person> getFriendSet() {
		return friendSet;
	}



}
