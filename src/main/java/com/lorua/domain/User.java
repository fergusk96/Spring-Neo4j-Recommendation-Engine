package com.lorua.domain;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class User {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String password;
	private String email;
	private List<String> roles;
	private boolean enabled;

	@Relationship(type = "HAS_VERIFICATION_TOKEN", direction = Relationship.OUTGOING)
	private VerificationToken verificationToken;
	

	@Relationship(type = "HAS_PASSWORD_TOKEN", direction = Relationship.OUTGOING)
	private User user;

	@Relationship(type = "IS_PERSON", direction = Relationship.OUTGOING)
	private Person person;

	public User() {
		this.enabled = false;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
