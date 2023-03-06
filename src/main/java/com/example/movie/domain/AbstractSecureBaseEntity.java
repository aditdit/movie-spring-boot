package com.example.movie.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Index;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@MappedSuperclass
@Table(indexes = { @Index(name = "uk_secure_id", columnList = "secure_id") })
public class AbstractSecureBaseEntity extends AbstractBaseEntity {

	private static final long serialVersionUID = -8667091878200405756L;

	@Column(name = "secure_id", nullable = false, unique = true)
	private String secureId = UUID.randomUUID().toString();

}
