package com.marketplace.company.service;

import com.marketplace.company.dto.CompanyRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.exception.CompanyAlreadyExistsException;
import com.marketplace.company.exception.CompanyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyService {

    Page<CompanyResponse> findAll(String companyName, Pageable pageable);

    Optional<CompanyResponse> findById(String companyId);

    CompanyResponse createCompany(String userId, CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException;

    void updateCompany(String companyId, CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException;
}
