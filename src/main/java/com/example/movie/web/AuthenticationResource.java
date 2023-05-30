package com.example.movie.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.auth.LoginRequestDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class AuthenticationResource {

	@PostMapping("/v1/login")
	public void login(@RequestBody LoginRequestDTO dto) {
	}

}
