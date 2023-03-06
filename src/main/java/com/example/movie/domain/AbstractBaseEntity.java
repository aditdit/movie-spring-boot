package com.example.movie.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class AbstractBaseEntity implements Serializable {

	private static final long serialVersionUID = -8667091878200405756L;

	@Column(name = "deleted", columnDefinition = "boolean default false")
	private boolean deleted;
	
	@CreationTimestamp
	private LocalDateTime createAt;
	
	@UpdateTimestamp
	private LocalDateTime updateAt;
}
