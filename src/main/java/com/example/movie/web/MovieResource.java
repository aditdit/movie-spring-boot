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

import com.example.movie.dto.MovieCreateRequestDTO;
import com.example.movie.dto.MovieDetailResponsetDTO;
import com.example.movie.dto.MovieLisResponsetDTO;
import com.example.movie.dto.MovieUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;
import com.example.movie.service.MovieService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class MovieResource {

	private final MovieService movieService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/v1/movie")
	public ResponseEntity<Void> createMovie(@RequestBody MovieCreateRequestDTO dto) {
		movieService.createMovie(dto);
		return ResponseEntity.created(URI.create("/v1/movie")).build();
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/movie")
	public ResponseEntity<ResultPageResponseDTO<MovieLisResponsetDTO>> findMovieList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "title") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "title", required = false, defaultValue = "") String movieTitle,
			@RequestParam(name = "genre", required = false, defaultValue = "") String movieGenre) {
		return ResponseEntity.ok().body(movieService.findMovieList(pages, limit, sortBy, direction, movieTitle, movieGenre));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/movie/{movieId}/detail")
	public ResponseEntity<MovieDetailResponsetDTO> findMovieDetail(@PathVariable Long movieId) {
		return ResponseEntity.ok().body(movieService.findMovieDetail(movieId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/v1/movie/{movieId}")
	public ResponseEntity<Void> updateMovie(@PathVariable Long movieId, @RequestBody MovieUpdateRequestDTO dto) {
		movieService.updateMovie(movieId, dto);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/v1/movie/{movieId}")
	public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
		movieService.deleteMovie(movieId);
		return ResponseEntity.ok().build();
	}

}
