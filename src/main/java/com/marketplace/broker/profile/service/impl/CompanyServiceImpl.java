package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.broker.profile.dto.CompanyRequest;
import com.marketplace.broker.profile.dto.CompanyResponse;
import com.marketplace.broker.profile.exception.CompanyAlreadyExistsException;
import com.marketplace.broker.profile.exception.CompanyNotFoundException;
import com.marketplace.broker.profile.service.CompanyService;
import com.marketplace.broker.profile.validator.CompanyValidator;
import com.marketplace.broker.profile.validator.VATValidator;
import com.marketplace.common.exception.BadRequestException;
import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Data
@Service
class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyResponseConverter companyResponseConverter;
    private final CompanyRequestConverter companyRequestConverter;
    private final VATValidator vatValidator;
    private final CompanyValidator companyValidator;
    private final PublishService publishService;

    @Override
    public Optional<CompanyResponse> findById(final String companyId) {
        return companyRepository.findById(companyId)
                .map(companyResponseConverter::convert);
    }

    @Override
    public CompanyResponse createCompany(final CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException {

        if (!companyRepository.findByCompanyNumberOrVatNumber(companyRequest.getCompanyNumber(), companyRequest.getVatNumber()).isEmpty()) {
            log.info("Company Number [ {} ] or VAT Number [ {} ] already exists", companyRequest.getCompanyNumber(), companyRequest.getVatNumber());
            throw new CompanyAlreadyExistsException(companyRequest.getCompanyNumber(), companyRequest.getVatNumber());
        }

        if (!vatValidator.validate(companyRequest.getName(), companyRequest.getVatNumber())) {
            new BadRequestException("Invalid VAT number");
        }

        if (!companyValidator.validate(companyRequest.getName(), companyRequest.getCompanyNumber())) {
            new BadRequestException("Invalid company number");
        }

        CompanyBO companyBO = companyRequestConverter.convert(companyRequest);
        companyBO = companyRepository.save(companyBO);
        final CompanyResponse companyResponse = companyResponseConverter.convert(companyBO);
        publishService.sendMessage(PublishAction.COMPANY_CREATED, companyResponse);
        return companyResponse;
    }

    @Override
    public void updateCompany(final String companyId, final CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException {

    }

    @Override
    public void inactivateCompany(final String companyId) {
        companyRepository.findById(companyId);
               // .map(companyBO -> companyBO.se)
    }
}
