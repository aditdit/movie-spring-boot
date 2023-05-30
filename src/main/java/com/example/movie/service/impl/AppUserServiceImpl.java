package com.example.movie.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.movie.domain.AppUser;
import com.example.movie.domain.Profile;
import com.example.movie.domain.Role;
import com.example.movie.dto.auth.UserRequestDTO;
import com.example.movie.repository.AppUserRepository;
import com.example.movie.repository.RoleRepository;
import com.example.movie.service.AppUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

	private final AppUserRepository appUserRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("invalid username"));
	}

	@Override
	public void createUser(UserRequestDTO dto) {
		Optional<AppUser> findUser = appUserRepository.findByUsername(dto.getUsername());
		
		if (findUser.isPresent()) {
			throw new UsernameNotFoundException("username.exists");
		} else {
			
			Role role = roleRepository.findByName("USER");

			Set<Role> roles = new HashSet<>();
			roles.add(role);

			Profile profile = new Profile();
			profile.setFullname(dto.getUsername());
			profile.setGender("");

			AppUser appUser = new AppUser();
			appUser.setUsername(dto.getUsername());
			appUser.setPassword(passwordEncoder.encode(dto.getPassword()));
			appUser.setEmail(dto.getEmail());
			appUser.setProfile(profile);
			appUser.setRoles(roles);

			appUserRepository.save(appUser);
		}

	}

}