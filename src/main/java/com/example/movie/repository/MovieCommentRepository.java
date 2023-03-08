package com.example.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.movie.domain.MovieComment;
import com.example.movie.dto.movie.MovieCommentQueryDTO;

public interface MovieCommentRepository extends JpaRepository<MovieComment, Long> {

	@Query("SELECT new com.example.movie.dto.movie.MovieCommentQueryDTO(mc.id, mcp.secureId, mcp.fullname, mc.comment) "
			+ "FROM MovieComment mc " 
			+ "JOIN mc.profile mcp "
			+ "JOIN mc.movie mcm "
			+ "WHERE mcm.id = :movieId")
	public Page<MovieCommentQueryDTO> findMovieCommentList(Long movieId, Pageable pageable);
	
}
