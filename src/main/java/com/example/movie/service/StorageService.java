package com.example.movie.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	
	public void save(MultipartFile file, Long movieId);
	
	public void deleteFile(Long id);
	
}
