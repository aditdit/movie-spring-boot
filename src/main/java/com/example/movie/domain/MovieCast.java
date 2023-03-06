package com.example.movie.domain;

import java.io.Serializable;

import com.example.movie.domain.key.MovieCastKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "movie_cast")
public class MovieCast implements Serializable {

	private static final long serialVersionUID = 4771008789939357469L;

	@EmbeddedId
	private MovieCastKey id;

	@Column(name = "character_name")
	private String characterName;
}
