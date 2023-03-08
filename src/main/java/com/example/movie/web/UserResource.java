package com.example.movie.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.auth.UserRequestDTO;
import com.example.movie.service.AppUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class UserResource {

	private final AppUserService appUserService;

	@PostMapping("/v1/registration")
	public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequestDTO dto) {
		appUserService.createUser(dto);
		return ResponseEntity.created(URI.create("/v1/registration")).build();
	}

}
