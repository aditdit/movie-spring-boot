package com.example.movie.dto.movie;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieCommentQueryDTO implements Serializable {

	private static final long serialVersionUID = 7687235016052091107L;

	private Long id;

	private String profileId;

	private String name;
	
	private String comment;
}
