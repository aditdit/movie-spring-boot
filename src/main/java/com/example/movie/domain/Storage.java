package com.example.movie.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "storage")
public class Storage implements Serializable {
	
	private static final long serialVersionUID = 8862638541251183121L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@Column(name = "path", nullable = false)
	private String path;
	
	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;
}
