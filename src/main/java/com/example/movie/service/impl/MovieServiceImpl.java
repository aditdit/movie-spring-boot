package com.example.movie.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.movie.config.DoConfig;
import com.example.movie.domain.AppUser;
import com.example.movie.domain.Company;
import com.example.movie.domain.Genre;
import com.example.movie.domain.Movie;
import com.example.movie.domain.MovieCast;
import com.example.movie.domain.MovieComment;
import com.example.movie.domain.Profile;
import com.example.movie.domain.key.MovieCastKey;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.company.CompanyLisResponsetDTO;
import com.example.movie.dto.genre.GenreLisResponsetDTO;
import com.example.movie.dto.movie.MovieCastCreateRequestDTO;
import com.example.movie.dto.movie.MovieCommentCreateRequestDTO;
import com.example.movie.dto.movie.MovieCommentListResponseDTO;
import com.example.movie.dto.movie.MovieCommentQueryDTO;
import com.example.movie.dto.movie.MovieCreateRequestDTO;
import com.example.movie.dto.movie.MovieDetailResponsetDTO;
import com.example.movie.dto.movie.MovieLisResponsetDTO;
import com.example.movie.dto.movie.MovieQueryDTO;
import com.example.movie.dto.movie.MovieUpdateRequestDTO;
import com.example.movie.dto.profile.ProfileLisResponsetDTO;
import com.example.movie.exception.BadRequestException;
import com.example.movie.repository.AppUserRepository;
import com.example.movie.repository.MovieCastRepository;
import com.example.movie.repository.MovieCommentRepository;
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

	private final MovieCommentRepository movieCommentRepository;

	private final GenreService genreService;

	private final CompanyService companyService;

	private final ProfileService profileService;

	private final AppUserRepository appUserRepository;

	private final DoConfig doConfig;

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
			List<MovieCastCreateRequestDTO> characterNameList = dto.getCasts().stream()
					.filter(item -> item.getId().equals(cast.getSecureId())).collect(Collectors.toList());

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
	public void createMovieComment(Long id, MovieCommentCreateRequestDTO dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUser = authentication.getName();

		AppUser profile = appUserRepository.findByUsername(currentUser)
				.orElseThrow(() -> new UsernameNotFoundException("invalid.user"));

		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));

		MovieComment movieComment = new MovieComment();
		movieComment.setComment(dto.comment());
		movieComment.setMovie(movie);
		movieComment.setProfile(profile.getProfile());

		movieCommentRepository.save(movieComment);
	}

	@Override
	public ResultPageResponseDTO<MovieLisResponsetDTO> findMovieList(Integer pages, Integer limit, String sortBy,
			String direction, String movieTitle, String movieGenre) {
		movieTitle = StringUtils.isEmpty(movieTitle) ? "%" : movieTitle + "%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);

		Page<MovieQueryDTO> pageResult = movieRepository.findMovieList(movieTitle, movieGenre, pageable);

		List<MovieLisResponsetDTO> dtos = pageResult.stream().map((m) -> {
			MovieLisResponsetDTO dto = new MovieLisResponsetDTO(m.getId(), m.getTitle(), m.getDescription(), "");
			return dto;
		}).collect(Collectors.toList());

		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Override
	public ResultPageResponseDTO<MovieCommentListResponseDTO> findMovieCommentList(Integer pages, Integer limit,
			String sortBy, String direction, Long movieId) {
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);

		Page<MovieCommentQueryDTO> pageResult = movieCommentRepository.findMovieCommentList(movieId, pageable);

		List<MovieCommentListResponseDTO> dtos = pageResult.stream().map((m) -> {
			MovieCommentListResponseDTO dto = new MovieCommentListResponseDTO(m.getId(), m.getProfileId(), m.getName(),
					m.getComment());
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

		List<String> storages = movie.getStorages().stream().map((s) -> {
			String url = doConfig.getS3Endpoint() + s.getName() + "." + s.getType();
			return url;
		}).collect(Collectors.toList());

		MovieDetailResponsetDTO dto = new MovieDetailResponsetDTO(movie.getId(), movie.getTitle(),
				movie.getDescription(), movie.getReleaseDate().toEpochDay(), movie.getBudget(), movie.getRevenue(),
				movie.getRating(), movie.getLanguage(), movie.getRuntime(), companies, genres, casts, casts, storages);

		return dto;
	}

	@Override
	public Movie findMovie(Long id) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));
		return movie;
	}

	@Override
	public void updateMovie(Long id, MovieUpdateRequestDTO dto) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));

		List<String> idCastList = dto.getCasts().stream().map((cast) -> {
			return cast.getId();
		}).collect(Collectors.toList());

		List<Genre> genres = genreService.findGenres(dto.getGenres());
		List<Company> companies = companyService.findCompanies(dto.getCompanies());
		List<Profile> casts = profileService.findProfiles(idCastList);
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

		List<MovieCast> movieCasts = casts.stream().map((cast) -> {
			List<MovieCastCreateRequestDTO> characterNameList = dto.getCasts().stream()
					.filter(item -> item.getId().equals(cast.getSecureId())).collect(Collectors.toList());

			MovieCastKey movieCastKey = new MovieCastKey();
			movieCastKey.setMovieId(id);
			movieCastKey.setProfileId(cast.getId());

			MovieCast movieCast = new MovieCast();
			movieCast.setId(movieCastKey);
			movieCast.setCharacterName(characterNameList.get(0).getCharacterName());

			return movieCast;
		}).collect(Collectors.toList());

		movieCastRepository.saveAll(movieCasts);
	}

	@Override
	public void deleteMovie(Long id) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new BadRequestException("invalid.movieId"));
		movieRepository.delete(movie);
	}

	@Override
	public void deleteMovieComment(Long id) {
		MovieComment movieComment = movieCommentRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("invalid.movieCommentId"));
		movieCommentRepository.delete(movieComment);
	}

}
