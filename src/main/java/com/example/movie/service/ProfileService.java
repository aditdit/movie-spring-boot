package com.example.movie.service;

import java.util.List;

import com.example.movie.domain.Profile;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.profile.ProfileCreateRequestDTO;
import com.example.movie.dto.profile.ProfileDetailResponseDTO;
import com.example.movie.dto.profile.ProfileLisResponsetDTO;
import com.example.movie.dto.profile.ProfileUpdateRequestDTO;

public interface ProfileService {

	public void createProfile(ProfileCreateRequestDTO dto);

	public ResultPageResponseDTO<ProfileLisResponsetDTO> findProfileList(Integer pages, Integer limit, String sortBy,
			String direction, String fullname);

	public List<Profile> findProfiles(List<String> profileIdList);

	
	public ProfileDetailResponseDTO findProfileDetail();
	
	public ProfileDetailResponseDTO findProfileDetailByProfileId(String profileId);
	
	public Profile findProfile(String profileId);

	public void updateProfile(String id, ProfileUpdateRequestDTO dto);
	
	public void updateProfileDetail(ProfileUpdateRequestDTO dto);

	public void deleteProfile(String id);

}
