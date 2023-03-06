package com.example.movie.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AnonymousAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -4721051034043552974L;

	public AnonymousAuthentication() {
		super(null);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}
}
