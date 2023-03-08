package com.example.movie.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.movie.security.filter.JWTAuthProcessingFilter;
import com.example.movie.security.filter.UsernamePasswordAuthProcessingFilter;
import com.example.movie.security.handler.UsernamePasswordAuthFailureHandler;
import com.example.movie.security.handler.UsernamePasswordAuthSuccessHandler;
import com.example.movie.security.provider.JwtAuthenticationProvider;
import com.example.movie.security.provider.UsernamePasswordAuthProvider;
import com.example.movie.security.util.JWTTokenFactory;
import com.example.movie.security.util.SkipPathRequestMatcher;
import com.example.movie.security.util.TokenExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	private final static String AUTH_URL = "/v1/login";

	private static final String[] AUTH_POST_WHITELIST = {
			"/v1/registration"
	};
	
	private static final String[] AUTH_GET_WHITELIST = {
			"/v1/storage/file/**",
			"/v1/movie/list",
			"/v1/movie/*/detail",
			"/v1/movie/*/comment",
			"/v1/genre/list"
	};
	
	private static final String[] SWAGGER_URL_WHITELIST = {
			"/swagger-ui.html",
	        "/v3/api-docs/**",
	        "v3/api-docs/**",
	        "/swagger-ui/**",
	        "swagger-ui/**"
	};
	
	private static final String[] AUTH_URL_LIST = {
			"/v1/**"
	};
	
	private final static List<String> PERMIT_ENDPOINT_LIST = new ArrayList<>();
	private final static List<String> AUTHENTICATED_ENDPOINT_LIST = Arrays.asList(AUTH_URL_LIST);

	@Autowired
	private UsernamePasswordAuthProvider usernamePasswordAuthProvider;

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Bean
	public AuthenticationSuccessHandler successHandler(ObjectMapper objectMapper, JWTTokenFactory tokenFactory) {
		return new UsernamePasswordAuthSuccessHandler(objectMapper, tokenFactory);
	}

	@Bean
	public AuthenticationFailureHandler failureHandler(ObjectMapper objectMapper) {
		return new UsernamePasswordAuthFailureHandler(objectMapper);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter(ObjectMapper objectMapper,
			AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler,
			AuthenticationManager authManager) {
		UsernamePasswordAuthProcessingFilter filter = new UsernamePasswordAuthProcessingFilter(AUTH_URL, objectMapper,
				successHandler, failureHandler);
		filter.setAuthenticationManager(authManager);
		return filter;
	}

	@Bean
	public JWTAuthProcessingFilter jwtAuthProcessingFilter(TokenExtractor tokenExtractor,
			AuthenticationFailureHandler failureHandler, AuthenticationManager authManager) {
		Collections.addAll(PERMIT_ENDPOINT_LIST, AUTH_URL);
		Collections.addAll(PERMIT_ENDPOINT_LIST, AUTH_POST_WHITELIST);
		Collections.addAll(PERMIT_ENDPOINT_LIST, AUTH_GET_WHITELIST);
		
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(PERMIT_ENDPOINT_LIST, AUTHENTICATED_ENDPOINT_LIST);
		JWTAuthProcessingFilter filter = new JWTAuthProcessingFilter(tokenExtractor, failureHandler, matcher);
		filter.setAuthenticationManager(authManager);
		return filter;
	}

	@Autowired
	void registerProvider(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(usernamePasswordAuthProvider).authenticationProvider(jwtAuthenticationProvider);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter,
			JWTAuthProcessingFilter jwtAuthProcessingFilter) throws Exception {
		
		http.authorizeHttpRequests()
			.requestMatchers(SWAGGER_URL_WHITELIST).permitAll()
			.requestMatchers(HttpMethod.POST, AUTH_POST_WHITELIST).permitAll()
			.requestMatchers(HttpMethod.GET, AUTH_GET_WHITELIST).permitAll()
			.requestMatchers(AUTH_URL_LIST).authenticated()
			.and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().httpBasic();

		http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
