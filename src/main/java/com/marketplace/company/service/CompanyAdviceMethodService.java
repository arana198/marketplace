package com.marketplace.company.service;

import com.marketplace.company.dto.CompanyAdviceMethodResponse;
import com.marketplace.company.exception.AdviceMethodNotFoundException;
import com.marketplace.company.exception.CompanyAdviceAlreadyExistsException;

public interface CompanyAdviceMethodService {

    CompanyAdviceMethodResponse getCompanyAdvice(String companyId);

    void addCompanyAdvice(String companyId, String adviceId) throws CompanyAdviceAlreadyExistsException, AdviceMethodNotFoundException;

    void removeCompanyAdvice(String companyId, String adviceId);

}
