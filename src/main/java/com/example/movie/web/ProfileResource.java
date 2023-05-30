package com.example.movie.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.profile.ProfileCreateRequestDTO;
import com.example.movie.dto.profile.ProfileDetailResponseDTO;
import com.example.movie.dto.profile.ProfileLisResponsetDTO;
import com.example.movie.dto.profile.ProfileUpdateRequestDTO;
import com.example.movie.service.ProfileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
public class ProfileResource {

	private final ProfileService profileService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/v1/profile")
	public ResponseEntity<Void> createProfile(@RequestBody @Valid ProfileCreateRequestDTO dto) {
		profileService.createProfile(dto);
		return ResponseEntity.created(URI.create("/v1/profile")).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/v1/profile")
	public ResponseEntity<ResultPageResponseDTO<ProfileLisResponsetDTO>> findProfileList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "fullname") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "desc") String direction,
			@RequestParam(name = "fullname", required = false) String fullname) {
		return ResponseEntity.ok().body(profileService.findProfileList(pages, limit, sortBy, direction, fullname));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/profile/detail")
	public ResponseEntity<ProfileDetailResponseDTO> findProfileDetail() {
		return ResponseEntity.ok().body(profileService.findProfileDetail());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/profile/{profileId}")
	public ResponseEntity<ProfileDetailResponseDTO> findProfileDetail(
			@PathVariable @Size(max = 36, min = 36, message = "profile.id.not.uuid") String profileId) {
		return ResponseEntity.ok().body(profileService.findProfileDetailByProfileId(profileId));
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/v1/profile")
	public ResponseEntity<Void> updateProfileDetail(
			@RequestBody @Valid ProfileUpdateRequestDTO dto) {
		profileService.updateProfileDetail(dto);
		return ResponseEntity.ok().build();
	}
		
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/v1/profile/{profileId}")
	public ResponseEntity<Void> updateProfile(
			@PathVariable @Size(max = 36, min = 36, message = "profile.id.not.uuid") String profileId,
			@RequestBody @Valid ProfileUpdateRequestDTO dto) {
		profileService.updateProfile(profileId, dto);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/v1/profile/{profileId}")
	public ResponseEntity<Void> deleteProfile(
			@PathVariable @Size(max = 36, min = 36, message = "profile.id.not.uuid") String profileId) {
		profileService.deleteProfile(profileId);
		return ResponseEntity.ok().build();
	}

}
