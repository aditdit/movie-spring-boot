package com.example.movie.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	private final static String V1_URL = "/v1/*";
	private final static String V2_URL = "/v2/*";

	private final static List<String> PERMIT_ENDPOINT_LIST = Arrays.asList(AUTH_URL);
	private final static List<String> AUTHENTICATED_ENDPOINT_LIST = Arrays.asList(V1_URL, V2_URL);

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
		http.authorizeHttpRequests().requestMatchers(V1_URL, V2_URL).authenticated().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().httpBasic();

		http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
