/**
 * PersonaService.java
 * 10/02/2014 19:46:01
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Persona;
import com.app.domain.repositories.PersonaRepository;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class PersonaService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2301222240849237937L;

	@Autowired
	/**
	 * 
	 */
	private PersonaRepository personaRepository;


	/**
	 * Constructor
	 */
	public PersonaService() {
		super();

	}

	// Métodos CRUD

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Persona> findAll() {
		List<Persona> list;
		list = personaRepository.findAll();

		return list;
	}

	// Other Business methods



	/**
	 * 
	 * @author David Romero Alcaide
	 * @param persona
	 */
	public void save(Persona persona) {
		Assert.notNull(persona);
		personaRepository.save(persona);
	}


}
