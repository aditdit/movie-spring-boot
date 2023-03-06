package com.example.movie.domain.key;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MovieCastKey implements Serializable{

	private static final long serialVersionUID = -2001451554002529479L;
	
	@Column(name = "movie_id")
	Long movieId;
	
	@Column(name = "profile_id")
	Long profileId;
}
