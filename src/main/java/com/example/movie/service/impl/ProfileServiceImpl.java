package com.example.movie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.movie.domain.AppUser;
import com.example.movie.domain.Profile;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.profile.ProfileCreateRequestDTO;
import com.example.movie.dto.profile.ProfileDetailResponseDTO;
import com.example.movie.dto.profile.ProfileLisResponsetDTO;
import com.example.movie.dto.profile.ProfileUpdateRequestDTO;
import com.example.movie.exception.BadRequestException;
import com.example.movie.repository.AppUserRepository;
import com.example.movie.repository.ProfileRepository;
import com.example.movie.service.ProfileService;
import com.example.movie.util.PaginationUtil;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	private final AppUserRepository appUserRepository;

	@Override
	public void createProfile(ProfileCreateRequestDTO dto) {
		Profile profile = new Profile();
		profile.setFullname(dto.fullname());
		profile.setGender(dto.gender());

		profileRepository.save(profile);
	}

	@Override
	public ResultPageResponseDTO<ProfileLisResponsetDTO> findProfileList(Integer pages, Integer limit, String sortBy,
			String direction, String fullname) {
		fullname = StringUtils.isEmpty(fullname) ? "%" : fullname + "%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);

		Page<Profile> pageResult = profileRepository.findByFullnameLikeIgnoreCase(fullname, pageable);

		List<ProfileLisResponsetDTO> dtos = pageResult.stream().map((p) -> {
			ProfileLisResponsetDTO dto = new ProfileLisResponsetDTO(p.getSecureId(), p.getFullname());
			return dto;
		}).collect(Collectors.toList());

		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Override
	public List<Profile> findProfiles(List<String> profileIdList) {
		List<Profile> profiles = profileRepository.findBySecureIdIn(profileIdList);
		if (profiles.isEmpty()) {
			throw new BadRequestException("profile cant empty");
		}
		return profiles;
	}

	@Override
	public Profile findProfile(String profileId) {
		Profile profile = profileRepository.findBySecureId(profileId)
				.orElseThrow(() -> new BadRequestException("invalid.profileId"));
		return profile;
	}

	@Override
	public ProfileDetailResponseDTO findProfileDetail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUser = authentication.getName();

		AppUser user = appUserRepository.findByUsername(currentUser)
				.orElseThrow(() -> new UsernameNotFoundException("invalid.user"));
		Profile profile = user.getProfile();
		ProfileDetailResponseDTO dto = new ProfileDetailResponseDTO(profile.getSecureId(), profile.getFullname(),
				profile.getGender());
		return dto;
	}

	@Override
	public ProfileDetailResponseDTO findProfileDetailByProfileId(String profileId) {
		Profile profile = profileRepository.findBySecureId(profileId)
				.orElseThrow(() -> new BadRequestException("invalid.profileId"));

		ProfileDetailResponseDTO dto = new ProfileDetailResponseDTO(profile.getSecureId(), profile.getFullname(),
				profile.getGender());

		return dto;
	}

	@Override
	public void updateProfile(String id, ProfileUpdateRequestDTO dto) {
		Profile profile = profileRepository.findBySecureId(id)
				.orElseThrow(() -> new BadRequestException("invalid.profileId"));
		profile.setFullname(dto.fullname());
		profile.setGender(dto.gender());
		profileRepository.save(profile);
	}

	@Override
	public void updateProfileDetail(ProfileUpdateRequestDTO dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUser = authentication.getName();

		AppUser user = appUserRepository.findByUsername(currentUser)
				.orElseThrow(() -> new UsernameNotFoundException("invalid.user"));

		Profile profile = user.getProfile();
		profile.setFullname(dto.fullname());
		profile.setGender(dto.gender());
		profileRepository.save(profile);
	}

	@Override
	public void deleteProfile(String id) {
		Profile profile = profileRepository.findBySecureId(id)
				.orElseThrow(() -> new BadRequestException("invalid.profileId"));
		profileRepository.delete(profile);
	}

}
