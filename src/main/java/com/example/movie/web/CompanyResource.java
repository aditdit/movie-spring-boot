package com.example.movie.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.CompanyCreateRequestDTO;
import com.example.movie.dto.CompanyLisResponsetDTO;
import com.example.movie.dto.CompanyUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;
import com.example.movie.service.CompanyService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CompanyResource {

	private final CompanyService companyService;

	@PostMapping("/v1/company")
	public ResponseEntity<Void> createCompany(@RequestBody CompanyCreateRequestDTO dto) {
		companyService.createCompany(dto);
		return ResponseEntity.created(URI.create("/v1/company")).build();
	}

	@GetMapping("/v1/company")
	public ResponseEntity<ResultPageResponseDTO<CompanyLisResponsetDTO>> findCompanyList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = true, defaultValue = "name") String sortBy,
			@RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name = "companyName", required = false) String companyName) {
		return ResponseEntity.ok().body(companyService.findCompanyList(pages, limit, sortBy, direction, companyName));
	}

	@PutMapping("/v1/company/{companyId}")
	public ResponseEntity<Void> updateCompany(@PathVariable Long companyId, @RequestBody CompanyUpdateRequestDTO dto) {
		companyService.updateCompany(companyId, dto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/v1/company/{companyId}")
	public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
		companyService.deleteCompany(companyId);
		return ResponseEntity.ok().build();
	}
}
