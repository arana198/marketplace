package com.marketplace.broker.profile.service;

import com.marketplace.broker.profile.dto.CompanyRequest;
import com.marketplace.broker.profile.dto.CompanyResponse;
import com.marketplace.broker.profile.exception.CompanyAlreadyExistsException;
import com.marketplace.broker.profile.exception.CompanyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyService {

    Page<CompanyResponse> findAll(String companyName, Pageable pageable);

    Optional<CompanyResponse> findById(String companyId);

    CompanyResponse createCompany(CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException;

    void updateCompany(String companyId, CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException;

    void inactivateCompany(String companyId);

}
