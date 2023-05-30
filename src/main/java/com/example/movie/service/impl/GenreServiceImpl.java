package com.example.movie.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movie.domain.Genre;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.genre.GenreCreateRequestDTO;
import com.example.movie.dto.genre.GenreLisResponsetDTO;
import com.example.movie.dto.genre.GenreUpdateRequestDTO;
import com.example.movie.exception.BadRequestException;
import com.example.movie.repository.GenreRepository;
import com.example.movie.service.GenreService;
import com.example.movie.util.PaginationUtil;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;

	@Override
	public void createGenre(GenreCreateRequestDTO dto) {
		Optional<Genre> findGenre = genreRepository.findByName(dto.name());

		if (!findGenre.isPresent()) {
			Genre genre = new Genre();
			genre.setName(dto.name());

			genreRepository.save(genre);
		}
	}

	@Override
	public ResultPageResponseDTO<GenreLisResponsetDTO> findGenreList(Integer pages, Integer limit, String sortBy,
			String direction, String genreName) {
		genreName = StringUtils.isEmpty(genreName) ? "%" : genreName + "%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);
		
		Page<Genre> pageResult = genreRepository.findByNameLikeIgnoreCase(genreName, pageable);

		List<GenreLisResponsetDTO> dtos = pageResult.stream().map((g) -> {
			GenreLisResponsetDTO dto = new GenreLisResponsetDTO(g.getId(), g.getName());
			return dto;
		}).collect(Collectors.toList());
		
		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}


	@Override
	public List<Genre> findGenres(List<Long> genreIdList) {
		List<Genre> genres = genreRepository.findByIdIn(genreIdList);
		if (genres.isEmpty()) {
			throw new BadRequestException("genre cant empty");
		}
		return genres;
	}
	
	@Override
	public void updateGenre(Long id, GenreUpdateRequestDTO dto) {
		Genre genre = genreRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.genreId"));
		genre.setName(dto.name());
		genreRepository.save(genre);
	}

	@Override
	public void deleteGenre(Long id) {
		Genre genre = genreRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.genreId"));
		genreRepository.delete(genre);
	}

}
