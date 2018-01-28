package com.marketplace.company.service;

import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.company.dto.CompanyServiceResponse;

public interface CompanyAdviceService {

    CompanyServiceResponse getCompanyServices(String companyId);

    void addCompanyServices(String companyId, String serviceId) throws ResourceNotFoundException, ResourceAlreadyExistsException;

    void removeCompanyServices(String companyId, String serviceId);

}
