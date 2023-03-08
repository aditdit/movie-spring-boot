package com.example.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.domain.Storage;

public interface StorageRepository extends JpaRepository<Storage, Long> {

}
