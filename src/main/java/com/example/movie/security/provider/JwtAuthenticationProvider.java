package com.example.movie.security.provider;

import java.security.Key;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.movie.security.model.JWTAuthenticationToken;
import com.example.movie.security.model.RawAccessJWTToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final Key key;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		RawAccessJWTToken token = (RawAccessJWTToken) authentication.getCredentials();
		Jws<Claims> jwsClaims = token.parseClaims(key);
		String subject = jwsClaims.getBody().getSubject();
		List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
		List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		UserDetails userDetails = new UserDetails() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public String getUsername() {
				return subject;
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return authorities;
			}
		};

		return new JWTAuthenticationToken(authorities, userDetails);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JWTAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
