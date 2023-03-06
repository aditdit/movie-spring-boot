package com.example.movie.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MovieUpdateRequestDTO {
	@NotBlank
	private String title;
	
	@NotBlank
	private String description;
	
	@NotNull
	private Long releaseDate;
	
	@NotNull
	private Long budget;
	
	@NotNull
	private Long revenue;
	
	@NotNull
	private Integer rating;
	
	@NotBlank
	private String language;
	
	@NotBlank
	private Integer runtime;
	
	@NotEmpty
	private List<Long> companies;
	
	@NotEmpty
	private List<Long> genres;
	
	@NotEmpty
	private List<String> directors;
	
	@NotEmpty
	private List<String> casts;
}
