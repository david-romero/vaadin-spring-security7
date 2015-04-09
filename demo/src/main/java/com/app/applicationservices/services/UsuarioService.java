/**
 * AdministradorService.java
 * appEducacional
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

import com.app.domain.model.types.Usuario;
import com.app.domain.repositories.UsuarioRepository;
import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.UserAccount;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class UsuarioService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2301222240849237937L;

	@Autowired
	/**
	 * 
	 */
	private UsuarioRepository usuarioRepository;


	/**
	 * Constructor
	 */
	public UsuarioService() {
		super();

	}

	// Métodos CRUD
	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Usuario create() {
		Usuario user = new Usuario();
		user.setIdentidadConfirmada(false);
		UserAccount account = new UserAccount();
		List<Authority> authorities = Lists.newArrayList();
		Authority auth = new Authority();
		auth.setAuthority(Authority.USUARIO);
		authorities.add(auth);
		account.setAuthorities(authorities);
		user.setUserAccount(account);

		return user;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Usuario> findAll() {
		List<Usuario> usuarios;
		usuarios = usuarioRepository.findAll();

		return usuarios;
	}

	// Other Business methods


	public Usuario findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);

		Usuario result;

		result = usuarioRepository.findByUserAccountId(userAccount
				.getId());
		
		Assert.notNull(result.getUserAccount());
		
		Assert.notEmpty(result.getUserAccount().getAuthorities());

		return result;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param admin
	 */
	public void save(Usuario user) {
		Assert.notNull(user);
		usuarioRepository.save(user);
	}


}
