package com.marketplace.company.service;

import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.dto.CompanyEmployeeRequest;
import com.marketplace.company.dto.CompanyEmployeeResponse;
import com.marketplace.company.exception.CompanyAlreadyExistsException;
import com.marketplace.company.exception.CompanyEmployeeInviteTokenNotFoundException;
import com.marketplace.company.exception.CompanyEmployeeNotFoundException;
import com.marketplace.company.exception.CompanyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyEmployeeService {

    Page<CompanyEmployeeResponse> findByCompanyId(String companyId, Pageable pageable);

    String addEmployeeToCompany(String companyId, CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException, CompanyEmployeeInviteTokenNotFoundException;

    void updateEmployeeInCompany(String companyId, String employeeId, CompanyEmployeeRequest companyEmployeeRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException, CompanyEmployeeNotFoundException;

    void removeEmployeeFromCompany(String companyId, String employeeId);

    void inviteEmployee(String companyId, CompanyEmployeeInviteRequest companyEmployeeInviteRequest) throws CompanyNotFoundException;
}
