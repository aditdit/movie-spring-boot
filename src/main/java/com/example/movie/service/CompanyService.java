package com.example.movie.service;

import java.util.List;

import com.example.movie.domain.Company;
import com.example.movie.dto.CompanyCreateRequestDTO;
import com.example.movie.dto.CompanyLisResponsetDTO;
import com.example.movie.dto.CompanyUpdateRequestDTO;
import com.example.movie.dto.ResultPageResponseDTO;

public interface CompanyService {

	public void createCompany(CompanyCreateRequestDTO dto);

	public ResultPageResponseDTO<CompanyLisResponsetDTO> findCompanyList(Integer pages, Integer limit, String sortBy,
			String direction, String companyName);

	public List<Company> findCompanies(List<Long> companyIdList);

	public void updateCompany(Long id, CompanyUpdateRequestDTO dto);

	public void deleteCompany(Long id);

}
