package com.marketplace.company.service;

import com.marketplace.company.dto.CompanyServiceResponse;
import com.marketplace.company.exception.CompanyServiceAlreadyExistsException;
import com.marketplace.company.exception.ServiceNotFoundException;

public interface CompanyProductService {

     CompanyServiceResponse getCompanyServices(String companyId);

     void addCompanyService(String companyId, String serviceId) throws CompanyServiceAlreadyExistsException, ServiceNotFoundException;

     void removeCompanyService(String companyId, String serviceId);

}
