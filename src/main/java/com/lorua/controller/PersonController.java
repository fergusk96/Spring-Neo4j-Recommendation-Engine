package com.lorua.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lorua.domain.Person;
import com.lorua.service.PersonService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class PersonController {

	private final PersonService personService;
	
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

    @GetMapping("/person/{name}")
	public Person findPerson(@PathVariable("name") String name) {
		return personService.findByTitle(name);
	}
}