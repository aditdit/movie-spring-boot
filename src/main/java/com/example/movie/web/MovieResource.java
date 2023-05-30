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
import com.example.movie.dto.movie.MovieCommentCreateRequestDTO;
import com.example.movie.dto.movie.MovieCommentListResponseDTO;
import com.example.movie.dto.movie.MovieCreateRequestDTO;
import com.example.movie.dto.movie.MovieDetailResponsetDTO;
import com.example.movie.dto.movie.MovieLisResponsetDTO;
import com.example.movie.dto.movie.MovieUpdateRequestDTO;
import com.example.movie.service.MovieService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Validated
public class MovieResource {

	private final MovieService movieService;

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/v1/movie")
	public ResponseEntity<Void> createMovie(@RequestBody @Valid MovieCreateRequestDTO dto) {
		movieService.createMovie(dto);
		return ResponseEntity.created(URI.create("/v1/movie")).build();
	}

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/v1/movie/{movieId}/comment")
	public ResponseEntity<Void> createMovieComment(@PathVariable Long movieId,
			@RequestBody @Valid MovieCommentCreateRequestDTO dto) {
		movieService.createMovieComment(movieId, dto);
		return ResponseEntity.created(URI.create("/v1/movie/" + movieId + "/comment")).build();
	}

	@GetMapping("/v1/movie/list")
	public ResponseEntity<ResultPageResponseDTO<MovieLisResponsetDTO>> findMovieList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "title") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "title", required = false, defaultValue = "") String movieTitle,
			@RequestParam(name = "genre", required = false, defaultValue = "") String movieGenre) {
		return ResponseEntity.ok()
				.body(movieService.findMovieList(pages, limit, sortBy, direction, movieTitle, movieGenre));
	}

	@GetMapping("/v1/movie/{movieId}/comment/list")
	public ResponseEntity<ResultPageResponseDTO<MovieCommentListResponseDTO>> findMovieCommentList(
			@PathVariable Long movieId,
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "title") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction) {
		return ResponseEntity.ok().body(movieService.findMovieCommentList(pages, limit, sortBy, direction, movieId));
	}

	@GetMapping("/v1/movie/{movieId}/detail")
	public ResponseEntity<MovieDetailResponsetDTO> findMovieDetail(@PathVariable Long movieId) {
		return ResponseEntity.ok().body(movieService.findMovieDetail(movieId));
	}

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/v1/movie/{movieId}")
	public ResponseEntity<Void> updateMovie(@PathVariable Long movieId, @RequestBody @Valid MovieUpdateRequestDTO dto) {
		movieService.updateMovie(movieId, dto);
		return ResponseEntity.ok().build();
	}

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/v1/movie/{movieId}")
	public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
		movieService.deleteMovie(movieId);
		return ResponseEntity.ok().build();
	}

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/v1/movie/comment/{movieCommentId}")
	public ResponseEntity<Void> deleteMovieComment(@PathVariable Long movieCommentId) {
		movieService.deleteMovieComment(movieCommentId);
		return ResponseEntity.ok().build();
	}

}
