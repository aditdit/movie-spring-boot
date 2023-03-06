package com.example.movie.config;

import java.security.Key;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.movie.security.util.JWTTokenFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class AppConfig {

	@Bean
	public Key key() {
		byte[] keyBytes = Decoders.BASE64.decode("asdke45fjbgt63jnft63ncdsg513fhakshfkh2ekhekdhsakdhkafs24");
		return Keys.hmacShaKeyFor(keyBytes); 
	}
	
	@Bean
	public JWTTokenFactory jwtTokenFactory(Key key) {
		return new JWTTokenFactory(key);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
}
