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
public class ErrorResponseDTO implements Serializable {

	private static final long serialVersionUID = -778768705105403025L;

	private Date timestamp;
	
	private String message;
	
	private ErrorCode errorCode;
	
	private List<String> details;
	
	private HttpStatus status;
	
	
	public static ErrorResponseDTO of(final String message, List<String> details, final ErrorCode errorCode, HttpStatus status) {
		return new ErrorResponseDTO(message, errorCode, details, status);
	}

	public ErrorResponseDTO(String message, ErrorCode errorCode, List<String> details, HttpStatus status) {
		super();
		this.message = message;
		this.errorCode = errorCode;
		this.details = details;
		this.status = status;
		this.timestamp = new Date();
	}

}
