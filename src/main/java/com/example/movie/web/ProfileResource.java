package com.example.movie.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.ProfileCreateRequestDTO;
import com.example.movie.dto.ProfileLisResponsetDTO;
import com.example.movie.dto.ProfileUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;
import com.example.movie.service.ProfileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class ProfileResource {

	private final ProfileService profileService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/v1/profile")
	public ResponseEntity<Void> createProfile(@RequestBody ProfileCreateRequestDTO dto) {
		profileService.createProfile(dto);
		return ResponseEntity.created(URI.create("/v1/profile")).build();
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/profile")
	public ResponseEntity<ResultPageResponseDTO<ProfileLisResponsetDTO>> findProfileList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "fullname") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "fullname", required = false) String fullname) {
		return ResponseEntity.ok().body(profileService.findProfileList(pages, limit, sortBy, direction, fullname));
	}
	
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/v1/profile/{profileId}")
	public ResponseEntity<Void> updateProfile(@PathVariable String profileId,
			@RequestBody ProfileUpdateRequestDTO dto) {
		profileService.updateProfile(profileId, dto);
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/v1/profile/{profileId}")
	public ResponseEntity<Void> deleteProfile(@PathVariable String profileId) {
		profileService.deleteProfile(profileId);
		return ResponseEntity.ok().build();
	}
	
}
