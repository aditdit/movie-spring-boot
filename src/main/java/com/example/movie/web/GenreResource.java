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
import com.example.movie.dto.genre.GenreCreateRequestDTO;
import com.example.movie.dto.genre.GenreLisResponsetDTO;
import com.example.movie.dto.genre.GenreUpdateRequestDTO;
import com.example.movie.service.GenreService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Validated
@AllArgsConstructor
@RestController
public class GenreResource {

	private final GenreService genreService;

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/v1/genre")
	public ResponseEntity<Void> createGenre(@RequestBody @Valid GenreCreateRequestDTO dto) {
		genreService.createGenre(dto);
		return ResponseEntity.created(URI.create("/v1/genre")).build();
	}

	@GetMapping("/v1/genre/list")
	public ResponseEntity<ResultPageResponseDTO<GenreLisResponsetDTO>> findGenreList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "name") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "genreName", required = false) String genreName) {
		return ResponseEntity.ok().body(genreService.findGenreList(pages, limit, sortBy, direction, genreName));
	}

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/v1/genre/{genreId}")
	public ResponseEntity<Void> updateGenre(@PathVariable Long genreId, @RequestBody @Valid GenreUpdateRequestDTO dto) {
		genreService.updateGenre(genreId, dto);
		return ResponseEntity.ok().build();
	}

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/v1/genre/{genreId}")
	public ResponseEntity<Void> deleteGenre(@PathVariable Long genreId) {
		genreService.deleteGenre(genreId);
		return ResponseEntity.ok().build();
	}

}
