/**
 * AuthManager.java
 * appEducacionalVaadin
 * 29/11/2014 14:50:06
 * Copyright David
 * com.app.infrastructure.security
 */
package com.app.infrastructure.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
/**
 * @author David
 *
 */
public class AuthManager implements AuthenticationManager,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6870121373591461123L;
	@Autowired
	private LoginService loginService;

	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		String username = (String) auth.getPrincipal();
		String password = (String) auth.getCredentials();
		UserDetails user = loginService.loadUserByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			Collection<? extends GrantedAuthority> authorities = user
					.getAuthorities();
			Credentials credentials = new Credentials();
			credentials.setJ_username(username);
			credentials.setPassword(password);
			return new UsernamePasswordAuthenticationToken(user, password,
					authorities);
		}
		throw new BadCredentialsException("Bad Credentials");
	}
	
	

}
