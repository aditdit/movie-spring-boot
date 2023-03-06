package com.example.movie.service;

import java.util.List;

import com.example.movie.domain.Profile;
import com.example.movie.dto.ProfileCreateRequestDTO;
import com.example.movie.dto.ProfileLisResponsetDTO;
import com.example.movie.dto.ProfileUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;

public interface ProfileService {

	public void createProfile(ProfileCreateRequestDTO dto);

	public ResultPageResponseDTO<ProfileLisResponsetDTO> findProfileList(Integer pages, Integer limit, String sortBy,
			String direction, String fullname);

	public List<Profile> findProfiles(List<String> profileIdList);

	public void updateProfile(String id, ProfileUpdateRequestDTO dto);

	public void deleteProfile(String id);

}
