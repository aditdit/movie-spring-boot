package com.example.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByName(String name);

}
