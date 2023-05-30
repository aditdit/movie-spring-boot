package com.example.movie.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.movie.service.StorageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class StorageResource {

	private final StorageService storageService;

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(path = "/v1/storage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadFile(@RequestPart("file") MultipartFile file,
			@RequestParam("movieId") Long movieId) {
		storageService.save(file, movieId);
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/v1/storage/{fileId}")
	public ResponseEntity<Void> deleteStorage(@PathVariable Long fileId) {
		storageService.deleteFile(fileId);
		return ResponseEntity.ok().build();
	}

}
