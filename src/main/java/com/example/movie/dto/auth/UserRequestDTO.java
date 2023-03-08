package com.example.movie.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequestDTO {

	@NotBlank
	@Size(min = 3)
	String username;

	@NotBlank
	@Size(min = 6)
	String password;

	@Email
	@NotBlank
	String email;

}
