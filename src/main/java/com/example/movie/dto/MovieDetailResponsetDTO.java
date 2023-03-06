package com.example.movie.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieDetailResponsetDTO(Long id, String title, String description, Long releaseDate, Long budget,
		Long revenue, Integer rating, String language, Integer runtime, List<CompanyLisResponsetDTO> companies,
		List<GenreLisResponsetDTO> genres, List<ProfileLisResponsetDTO> directors, List<ProfileLisResponsetDTO> casts) {
}
