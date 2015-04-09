/**
 * PersonRepository.java
 * appEducacional
 * 14/01/2014 10:25:27
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

/**
 * imports
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.Persona;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

}
