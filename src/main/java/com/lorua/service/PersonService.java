package com.lorua.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lorua.domain.Person;
import com.lorua.repository.PersonRepository;

public class PersonService {

	@Autowired
	private final PersonRepository personRepository;

	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Transactional(readOnly = true)
	public Person findByTitle(String name) {
		return personRepository.findByName(name);
	}

	@Transactional(readOnly = true)
	public Collection<Person> findByTitleLike(String name) {
		return personRepository.findByNameLike(name);
	}



}
