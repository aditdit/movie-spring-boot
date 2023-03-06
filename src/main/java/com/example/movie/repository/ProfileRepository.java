package com.example.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	public Optional<Profile> findBySecureId(String id);

	public List<Profile> findBySecureIdIn(List<String> profileIdList);

	public Page<Profile> findByFullnameLikeIgnoreCase(String fullname, Pageable pageable);
}
