package com.example.movie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movie.domain.Company;
import com.example.movie.dto.CompanyCreateRequestDTO;
import com.example.movie.dto.CompanyLisResponsetDTO;
import com.example.movie.dto.CompanyUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;
import com.example.movie.exception.BadRequestException;
import com.example.movie.repository.CompanyRepository;
import com.example.movie.service.CompanyService;
import com.example.movie.util.PaginationUtil;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;

	@Override
	public void createCompany(CompanyCreateRequestDTO dto) {
		Company company = new Company();
		company.setName(dto.name());
		companyRepository.save(company);
	}

	@Override
	public ResultPageResponseDTO<CompanyLisResponsetDTO> findCompanyList(Integer pages, Integer limit, String sortBy,
			String direction, String companyName) {
		companyName = StringUtils.isEmpty(companyName) ? "%" : companyName + "%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(sortBy), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);
		Page<Company> pageResult = companyRepository.findByNameLikeIgnoreCase(companyName, pageable);

		List<CompanyLisResponsetDTO> dtos = pageResult.stream().map((c) -> {
			CompanyLisResponsetDTO dto = new CompanyLisResponsetDTO(c.getId(), c.getName());
			return dto;
		}).collect(Collectors.toList());
		return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
	}

	@Override
	public List<Company> findCompanies(List<Long> companyIdList) {
		List<Company> companies = companyRepository.findByIdIn(companyIdList);
		log.info("companies.isEmpty() : " + companies.isEmpty());
		if (companies.isEmpty()) {
			throw new BadRequestException("company can't empty");
		}
		return companies;
	}

	@Override
	public void updateCompany(Long id, CompanyUpdateRequestDTO dto) {
		Company company = companyRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("invalid.companyId"));
		company.setName(dto.name());
		companyRepository.save(company);
	}

	@Override
	public void deleteCompany(Long id) {
		Company company = companyRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("invalid.companyId"));
		companyRepository.delete(company);
	}

}
