package com.example.movie.service;

import com.example.movie.dto.MovieCreateRequestDTO;
import com.example.movie.dto.MovieDetailResponsetDTO;
import com.example.movie.dto.MovieLisResponsetDTO;
import com.example.movie.dto.MovieUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;

public interface MovieService {
	public void createMovie(MovieCreateRequestDTO dto);

	public ResultPageResponseDTO<MovieLisResponsetDTO> findMovieList(Integer pages, Integer limit, String sortBy,
			String direction, String movieTitle, String movieGenre);

	public MovieDetailResponsetDTO findMovieDetail(Long id);
	
	public void updateMovie(Long id, MovieUpdateRequestDTO dto);

	public void deleteMovie(Long id);
}
