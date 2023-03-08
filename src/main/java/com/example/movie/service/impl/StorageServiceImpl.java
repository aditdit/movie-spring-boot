package com.example.movie.service.impl;

import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.movie.domain.Movie;
import com.example.movie.domain.Storage;
import com.example.movie.repository.StorageRepository;
import com.example.movie.service.MovieService;
import com.example.movie.service.StorageService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {

	private final Path root = Paths.get("uploads");

	private final StorageRepository storageRepository;

	private final MovieService movieService;

	@Override
	public void save(MultipartFile file, Long movieId) {
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

			Movie movie = movieService.findMovie(movieId);
			Storage storage = new Storage();
			storage.setType(file.getContentType());
			storage.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			storage.setMovie(movie);

			storageRepository.save(storage);
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
}
