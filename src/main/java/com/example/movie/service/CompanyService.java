package com.example.movie.service;

import java.util.List;

import com.example.movie.domain.Company;
import com.example.movie.dto.common.ResultPageResponseDTO;
import com.example.movie.dto.company.CompanyCreateRequestDTO;
import com.example.movie.dto.company.CompanyLisResponsetDTO;
import com.example.movie.dto.company.CompanyUpdateRequestDTO;

public interface CompanyService {

	public void createCompany(CompanyCreateRequestDTO dto);

	public ResultPageResponseDTO<CompanyLisResponsetDTO> findCompanyList(Integer pages, Integer limit, String sortBy,
			String direction, String companyName);

	public List<Company> findCompanies(List<Long> companyIdList);

	public void updateCompany(Long id, CompanyUpdateRequestDTO dto);

	public void deleteCompany(Long id);

}
