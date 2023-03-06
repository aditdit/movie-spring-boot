package com.example.movie.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.movie.security.model.AccessJWTToken;
import com.example.movie.security.util.JWTTokenFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class UsernamePasswordAuthSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper mapper;
	
	private final JWTTokenFactory tokenFactory;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		AccessJWTToken token = tokenFactory.createAcessJWTToken(userDetails.getUsername(), userDetails.getAuthorities());
		
		Map<String, String> resulstMap = new HashMap<>();
		resulstMap.put("token", token.getToken());
		
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		mapper.writeValue(response.getWriter(), resulstMap);
		clearAuthenticationAttributes(request);
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if (session == null) {
			return;
		}
		
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);		
	}
	
}
