package com.example.movie.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.movie.dto.auth.UserRequestDTO;

public interface AppUserService extends UserDetailsService {

	public void createUser(UserRequestDTO dto);
	
}
