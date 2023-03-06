package com.example.movie.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.movie.repository.AppUserRepository;
import com.example.movie.service.AppUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

	private final AppUserRepository appUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("invalid username"));
	}

}