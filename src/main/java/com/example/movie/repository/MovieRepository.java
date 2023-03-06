package com.example.movie.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.movie.domain.Movie;
import com.example.movie.dto.MovieQueryDTO;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	public Optional<Movie> findById(Long id);

	public Page<Movie> findByTitleLikeIgnoreCase(String title, Pageable pageable);

	@Query("SELECT DISTINCT new com.example.movie.dto.MovieQueryDTO(m.id, m.title, m.description, m.rating) "
			+ "FROM Movie m "
			+ "JOIN m.genres mg "
			+ "WHERE LOWER(m.title) LIKE LOWER(CONCAT('%',:movieTitle,'%')) "
			+ "AND LOWER(mg.name) LIKE LOWER(CONCAT('%',:movieGenre,'%'))")
	public Page<MovieQueryDTO> findMovieList(String movieTitle, String movieGenre, Pageable pageable);
}
