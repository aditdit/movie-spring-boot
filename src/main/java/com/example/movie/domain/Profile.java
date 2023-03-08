package com.example.movie.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "profile")
@SQLDelete(sql = "UPDATE profile SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Profile extends AbstractSecureBaseEntity {

	private static final long serialVersionUID = 8805016793743438918L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "fullname", nullable = false)
	private String fullname;

	@Column(name = "gender", nullable = false)
	private String gender;

}
