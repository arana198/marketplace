package com.marketplace.broker.profile.service;

import com.marketplace.broker.profile.dto.CompanyEmployeeInviteRequest;
import com.marketplace.broker.profile.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.broker.profile.dto.CompanyEmployeeRequest;
import com.marketplace.broker.profile.dto.CompanyEmployeeResponse;
import com.marketplace.broker.profile.exception.CompanyAlreadyExistsException;
import com.marketplace.broker.profile.exception.CompanyEmployeeInviteTokenNotFoundException;
import com.marketplace.broker.profile.exception.CompanyEmployeeNotFoundException;
import com.marketplace.broker.profile.exception.CompanyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyEmployeeService {

    Page<CompanyEmployeeResponse> findByCompanyId(String companyId, Pageable pageable);

    String addEmployeeToCompany(String companyId, CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException, CompanyEmployeeInviteTokenNotFoundException;

    void updateEmployeeInCompany(String companyId, String employeeId, CompanyEmployeeRequest companyEmployeeRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException, CompanyEmployeeNotFoundException;

    void removeEmployeeFromCompany(String companyId, String employeeId);

    void inviteEmployee(String companyId, CompanyEmployeeInviteRequest companyEmployeeInviteRequest) throws CompanyNotFoundException;
}
