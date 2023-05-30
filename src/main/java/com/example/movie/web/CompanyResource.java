package com.example.movie.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.company.CompanyCreateRequestDTO;
import com.example.movie.dto.company.CompanyLisResponsetDTO;
import com.example.movie.dto.company.CompanyUpdateRequestDTO;
import com.example.movie.service.CompanyService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CompanyResource {

	private final CompanyService companyService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/v1/company")
	public ResponseEntity<Void> createCompany(@RequestBody @Valid CompanyCreateRequestDTO dto) {
		companyService.createCompany(dto);
		return ResponseEntity.created(URI.create("/v1/company")).build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/v1/company")
	public ResponseEntity<ResultPageResponseDTO<CompanyLisResponsetDTO>> findCompanyList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "name") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "companyName", required = false) String companyName) {
		return ResponseEntity.ok().body(companyService.findCompanyList(pages, limit, sortBy, direction, companyName));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/v1/company/{companyId}")
	public ResponseEntity<Void> updateCompany(@PathVariable Long companyId,
			@RequestBody @Valid CompanyUpdateRequestDTO dto) {
		companyService.updateCompany(companyId, dto);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/v1/company/{companyId}")
	public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
		companyService.deleteCompany(companyId);
		return ResponseEntity.ok().build();
	}
}
