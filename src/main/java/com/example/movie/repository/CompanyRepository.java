package com.example.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	public Optional<Company> findById(Long id);
	
	public Optional<Company> findByName(String name);

	public List<Company> findByIdIn(List<Long> companyIdList);

	public Page<Company> findByNameLikeIgnoreCase(String name, Pageable pageable);

}
