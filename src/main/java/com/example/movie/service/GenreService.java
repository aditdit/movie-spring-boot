package com.example.movie.service;

import java.util.List;

import com.example.movie.domain.Genre;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.genre.GenreCreateRequestDTO;
import com.example.movie.dto.genre.GenreLisResponsetDTO;
import com.example.movie.dto.genre.GenreUpdateRequestDTO;

public interface GenreService {

	public void createGenre(GenreCreateRequestDTO dto);

	public ResultPageResponseDTO<GenreLisResponsetDTO> findGenreList(Integer pages, Integer limit, String sortBy,
			String direction, String genreName);

	public List<Genre> findGenres(List<Long> genreIdList);

	public void updateGenre(Long id, GenreUpdateRequestDTO dto);

	public void deleteGenre(Long id);

}
