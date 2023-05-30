package com.example.movie.service.impl;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.movie.config.DoConfig;
import com.example.movie.domain.Movie;
import com.example.movie.domain.Storage;
import com.example.movie.exception.BadRequestException;
import com.example.movie.repository.StorageRepository;
import com.example.movie.service.MovieService;
import com.example.movie.service.StorageService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {

	private final StorageRepository storageRepository;

	private final MovieService movieService;

	private final DoConfig doConfig;

	private final String FOLDER = "files/";

	@Override
	public void save(MultipartFile file, Long movieId) {
		try {
			String key = FOLDER + file.getOriginalFilename();
			saveImageToServer(file, key);

			Movie movie = movieService.findMovie(movieId);
			Storage storage = new Storage();
			storage.setType(file.getContentType());
			storage.setName(file.getOriginalFilename());
			storage.setMovie(movie);

			storageRepository.save(storage);
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}
	}

	private void saveImageToServer(MultipartFile multipartFile, String key) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getInputStream().available());
		if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
			metadata.setContentType(multipartFile.getContentType());
		}
		doConfig.getS3().putObject(
				new PutObjectRequest(doConfig.getDoSpaceBucket(), key, multipartFile.getInputStream(), metadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	@Override
	public void deleteFile(Long fileId) {
		try {
			Storage storage = storageRepository.findById(fileId)
					.orElseThrow(() -> new BadRequestException("invalid.fileId"));

			String key = FOLDER + storage.getName();

			doConfig.getS3().deleteObject(new DeleteObjectRequest(doConfig.getDoSpaceBucket(), key));
			storageRepository.delete(storage);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}
}
