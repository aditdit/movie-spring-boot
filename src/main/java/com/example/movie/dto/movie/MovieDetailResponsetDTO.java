package com.example.movie.dto.movie;

import java.math.BigDecimal;
import java.util.List;

import com.example.movie.dto.company.CompanyLisResponsetDTO;
import com.example.movie.dto.genre.GenreLisResponsetDTO;
import com.example.movie.dto.profile.ProfileLisResponsetDTO;
import com.example.movie.dto.storage.StorageListResponseDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieDetailResponsetDTO(Long id, String title, String description, Long releaseDate, BigDecimal budget,
		BigDecimal revenue, Double rating, String language, Integer runtime, List<CompanyLisResponsetDTO> companies,
		List<GenreLisResponsetDTO> genres, List<ProfileLisResponsetDTO> directors, List<ProfileLisResponsetDTO> casts,
		List<String> storages) {
}
