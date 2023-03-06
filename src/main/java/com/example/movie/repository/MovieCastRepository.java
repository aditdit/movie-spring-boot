package com.example.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.domain.MovieCast;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long> {

}
