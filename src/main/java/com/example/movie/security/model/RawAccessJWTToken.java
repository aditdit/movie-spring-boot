package com.example.movie.security.model;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RawAccessJWTToken implements Token {

	private String token;
	
	public Jws<Claims> parseClaims(Key signingKey) {
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(this.token);
	}
}
