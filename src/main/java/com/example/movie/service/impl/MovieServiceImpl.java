package com.example.movie.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movie.domain.Company;
import com.example.movie.domain.Genre;
import com.example.movie.domain.Movie;
import com.example.movie.domain.MovieCast;
import com.example.movie.domain.Profile;
import com.example.movie.domain.key.MovieCastKey;
import com.example.movie.dto.CompanyLisResponsetDTO;
import com.example.movie.dto.GenreLisResponsetDTO;
import com.example.movie.dto.MovieCastCreateRequestDto;
import com.example.movie.dto.MovieCreateRequestDTO;
import com.example.movie.dto.MovieDetailResponsetDTO;
import com.example.movie.dto.MovieLisResponsetDTO;
import com.example.movie.dto.MovieQueryDTO;
import com.example.movie.dto.MovieUpdateRequestDTO;
import com.example.movie.dto.ProfileLisResponsetDTO;
import com.example.movie.dto.ResultPageResponseDTO;
import com.example.movie.exception.BadRequestException;
import com.example.movie.repository.MovieCastRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.service.CompanyService;
import com.example.movie.service.GenreService;
import com.example.movie.service.MovieService;
import com.example.movie.service.ProfileService;
import com.example.movie.util.PaginationUtil;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

	private final MovieRepository movieRepository;

	private final MovieCastRepository movieCastRepository;

	private final GenreService genreService;

	private final CompanyService companyService;

	private final ProfileService profileService;

	@Override
	public void createMovie(MovieCreateRequestDTO dto) {
		List<String> idCastList = dto.getCasts().stream().map((cast) -> {
			return cast.getId();
		}).collect(Collectors.toList());

		List<Genre> genres = genreService.findGenres(dto.getGenres());
		List<Company> companies = companyService.findCompanies(dto.getCompanies());
		List<Profile> casts = profileService.findProfiles(idCastList);
		List<Profile> directors = profileService.findProfiles(dto.getDirectors());

		Movie movie = new Movie();
		movie.setTitle(dto.getTitle());
		movie.setDescription(dto.getDescription());
		movie.setReleaseDate(LocalDate.ofEpochDay(dto.getReleaseDate()));
		movie.setBudget(dto.getBudget());
		movie.setRevenue(dto.getRevenue());
		movie.setRating(dto.getRating());
		movie.setLanguage(dto.getLanguage());
		movie.setRuntime(dto.getRuntime());
		movie.setGenres(genres);
		movie.setCompanies(companies);
		movie.setDirectors(directors);

		final Movie savedMovie = movieRepository.saveAndFlush(movie);

		List<MovieCast> movieCasts = casts.stream().map((cast) -> {
			List<MovieCastCreateRequestDto> characterNameList = dto.getCasts().stream()
					.filter(item -> item.getId().equals(cast.getSecureId()))
					.collect(Collectors.toList());
			
			MovieCastKey movieCastKey = new MovieCastKey();
			movieCastKey.setMovieId(savedMovie.getId());
			movieCastKey.setProfileId(cast.getId());

			MovieCast movieCast = new MovieCast();
			movieCast.setId(movieCastKey);
			movieCast.setCharacterName(characterNameList.get(0).getCharacterName());

			return movieCast;
		}).collect(Collectors.toList());

		movieCastRepository.saveAll(movieCasts);
	}

	@Override
	public ResultPageResponseDTO<MovieLisResponsetDTO> findMovieList(Integer pages, Integer limit, String sortBy,
			String direction, String movieTitle, String movieGenre) {
		movieTitle = StringUtils.isEmpty(movieTitle) ? "%" : movieTitle + "%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);
		Page<MovieQueryDTO> pageResult = movieRepository.findMovieList(movieTitle, movieGenre, pageable);
		List<MovieLisResponsetDTO> dtos = pageResult.stream().map((m) -> {
			MovieLisResponsetDTO dto = new MovieLisResponsetDTO(m.getId(), m.getTitle(), m.getDescription());
			return dto;
		}).collect(Collectors.toList());
		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Override
	public MovieDetailResponsetDTO findMovieDetail(Long id) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));

		List<GenreLisResponsetDTO> genres = movie.getGenres().stream().map((g) -> {
			GenreLisResponsetDTO genre = new GenreLisResponsetDTO(g.getId(), g.getName());
			return genre;
		}).collect(Collectors.toList());

		List<CompanyLisResponsetDTO> companies = movie.getCompanies().stream().map((c) -> {
			CompanyLisResponsetDTO company = new CompanyLisResponsetDTO(c.getId(), c.getName());
			return company;
		}).collect(Collectors.toList());

		List<ProfileLisResponsetDTO> casts = movie.getCasts().stream().map((c) -> {
			ProfileLisResponsetDTO cast = new ProfileLisResponsetDTO(c.getSecureId(), c.getFullname());
			return cast;
		}).collect(Collectors.toList());

		MovieDetailResponsetDTO dto = new MovieDetailResponsetDTO(movie.getId(), movie.getTitle(),
				movie.getDescription(), movie.getReleaseDate().toEpochDay(), movie.getBudget(), movie.getRevenue(),
				movie.getRating(), movie.getLanguage(), movie.getRuntime(), companies, genres, casts, casts);

		return dto;
	}

	@Override
	public void updateMovie(Long id, MovieUpdateRequestDTO dto) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));

		List<Genre> genres = genreService.findGenres(dto.getGenres());
		List<Company> companies = companyService.findCompanies(dto.getCompanies());
		List<Profile> casts = profileService.findProfiles(dto.getCasts());
		List<Profile> directors = profileService.findProfiles(dto.getDirectors());

		movie.setTitle(dto.getTitle());
		movie.setDescription(dto.getDescription());
		movie.setReleaseDate(LocalDate.ofEpochDay(dto.getReleaseDate()));
		movie.setBudget(dto.getBudget());
		movie.setRevenue(dto.getRevenue());
		movie.setRating(dto.getRating());
		movie.setLanguage(dto.getLanguage());
		movie.setRuntime(dto.getRuntime());
		movie.setGenres(genres);
		movie.setCompanies(companies);
		movie.setCasts(casts);
		movie.setDirectors(directors);

		movieRepository.save(movie);
	}

	@Override
	public void deleteMovie(Long id) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));
		movieRepository.delete(movie);
	}

}
