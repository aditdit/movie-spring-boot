package com.example.movie.dto.auth;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginRequestDTO implements Serializable {

	private static final long serialVersionUID = -4375558406650911797L;

	private String username;
	
	private String password;
}
