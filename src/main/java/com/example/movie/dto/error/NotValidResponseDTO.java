package com.example.movie.dto.error;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotValidResponseDTO implements Serializable {

	private static final long serialVersionUID = -1071959715096821108L;

	private String code;

	private String rejectedValue;

	private String property;

	private String message;

}
