package com.example.movie.dto.error;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.example.movie.enums.ErrorCode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvalidDataErrorResponseDTO implements Serializable {

	private static final long serialVersionUID = 7399564931453165623L;

	private Date timestamp;

	private String message;

	private ErrorCode errorCode;

	private List<NotValidResponseDTO> details;

	private HttpStatus status;

	public static InvalidDataErrorResponseDTO of(final String message, List<NotValidResponseDTO> details,
			final ErrorCode errorCode, HttpStatus status) {
		return new InvalidDataErrorResponseDTO(message, errorCode, details, status);
	}

	public InvalidDataErrorResponseDTO(String message, ErrorCode errorCode, List<NotValidResponseDTO> details,
			HttpStatus status) {
		super();
		this.message = message;
		this.errorCode = errorCode;
		this.details = details;
		this.status = status;
		this.timestamp = new Date();
	}
}
