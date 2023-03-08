package com.example.movie.dto.movie;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieQueryDTO implements Serializable {
	private static final long serialVersionUID = 6729423159660574331L;

	private Long id;

	private String title;

	private String description;
	
	private Double rating;
}
