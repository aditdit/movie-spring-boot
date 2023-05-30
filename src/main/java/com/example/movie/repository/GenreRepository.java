package com.example.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	public Optional<Genre> findById(Long id);

	public Optional<Genre> findByName(String name);

	public List<Genre> findByIdIn(List<Long> genreIdList);

	public Page<Genre> findByNameLikeIgnoreCase(String name, Pageable pageable);
}
