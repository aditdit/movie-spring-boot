package com.example.movie.security.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1935848053299298878L;

	private RawAccessJWTToken rawAccessJWTToken;

	private UserDetails userDetails;

	public JWTAuthenticationToken(RawAccessJWTToken rawAccessJWTToken) {
		super(null);
		this.rawAccessJWTToken = rawAccessJWTToken;
		this.setAuthenticated(false);
	}

	public JWTAuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserDetails userDetails) {
		super(authorities);
		this.eraseCredentials();
		this.userDetails = userDetails;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.rawAccessJWTToken;
	}

	@Override
	public Object getPrincipal() {
		return this.userDetails;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.rawAccessJWTToken = null;
	}

}
