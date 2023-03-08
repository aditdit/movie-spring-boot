package com.example.movie.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	public void save(MultipartFile file, Long movieId);

	public Resource load(String filename);
}
