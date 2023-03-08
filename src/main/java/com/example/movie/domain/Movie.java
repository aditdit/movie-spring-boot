package com.example.movie.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "movie")
@SQLDelete(sql = "UPDATE movie SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Movie extends AbstractBaseEntity {

	private static final long serialVersionUID = -8391949470386500655L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "release_date", nullable = false)
	private LocalDate releaseDate;

	@Column(name = "budget", nullable = false)
	private BigDecimal budget;

	@Column(name = "revenue", nullable = false)
	private BigDecimal revenue;

	@Column(name = "rating")
	private Double rating;

	@Column(name = "language", nullable = false)
	private String language;

	@Column(name = "runtime", nullable = false)
	private Integer runtime;

	@OneToMany(mappedBy = "movie")
	private List<Storage> storages;

	@ManyToMany
	@JoinTable(name = "movie_genre", joinColumns = {
			@JoinColumn(name = "movie_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "genre_id", referencedColumnName = "id") })
	private List<Genre> genres;

	@ManyToMany
	@JoinTable(name = "movie_company", joinColumns = {
			@JoinColumn(name = "movie_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "company_id", referencedColumnName = "id") })
	private List<Company> companies;

	@ManyToMany
	@JoinTable(name = "movie_director", joinColumns = {
			@JoinColumn(name = "movie_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "profile_id", referencedColumnName = "id") })
	private List<Profile> directors;

	@ManyToMany
	@JoinTable(name = "movie_cast", joinColumns = {
			@JoinColumn(name = "movie_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "profile_id", referencedColumnName = "id") })
	private List<Profile> casts;

}
