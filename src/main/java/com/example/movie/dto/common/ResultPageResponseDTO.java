package com.example.movie.dto.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ResultPageResponseDTO<T> implements Serializable {
	
	private static final long serialVersionUID = -6139667613771715496L;

	private List<T> result;

	private Integer pages;

	private Long elements;
}
