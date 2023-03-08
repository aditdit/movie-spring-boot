package com.example.movie.service;

import com.example.movie.domain.Movie;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.movie.MovieCommentCreateRequestDTO;
import com.example.movie.dto.movie.MovieCommentListResponseDTO;
import com.example.movie.dto.movie.MovieCreateRequestDTO;
import com.example.movie.dto.movie.MovieDetailResponsetDTO;
import com.example.movie.dto.movie.MovieLisResponsetDTO;
import com.example.movie.dto.movie.MovieUpdateRequestDTO;

public interface MovieService {
	public void createMovie(MovieCreateRequestDTO dto);
	
	public void createMovieComment(Long id, MovieCommentCreateRequestDTO dto);

	public ResultPageResponseDTO<MovieLisResponsetDTO> findMovieList(Integer pages, Integer limit, String sortBy,
			String direction, String movieTitle, String movieGenre);

	public ResultPageResponseDTO<MovieCommentListResponseDTO> findMovieCommentList(Integer pages, Integer limit, String sortBy,
			String direction, Long movieId);

	public MovieDetailResponsetDTO findMovieDetail(Long id);
	
	public Movie findMovie(Long id);
	
	public void updateMovie(Long id, MovieUpdateRequestDTO dto);

	public void deleteMovie(Long id);
	
	public void deleteMovieComment(Long id);
}
