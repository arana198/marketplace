package com.marketplace.company.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.company.domain.BrokerProfileBO;
import com.marketplace.company.domain.CompanyBO;
import com.marketplace.company.dto.CompanyRegistrationRequest;
import com.marketplace.company.dto.CompanyRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.exception.CompanyAlreadyExistsException;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.service.CompanyService;
import com.marketplace.company.validator.CompanyValidator;
import com.marketplace.company.validator.VATValidator;
import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Data
@Service
class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final BrokerProfileRepository brokerProfileRepository;
    private final CompanyResponseConverter companyResponseConverter;
    private final CompanyRequestConverter companyRequestConverter;
    private final BrokerProfileRequestConverter brokerProfileRequestConverter;
    private final VATValidator vatValidator;
    private final CompanyValidator companyValidator;
    private final PublishService publishService;

    @Override
    public Page<CompanyResponse> findAll(final String companyName, final Pageable pageable) {
        return Optional.ofNullable(companyName)
                .map(cn -> this.companyRepository.findByFulltextName(cn, pageable))
                .orElse(this.companyRepository.findAll(pageable))
                .map(this.companyResponseConverter::convert);
    }

    @Override
    public Optional<CompanyResponse> findById(final String companyId) {
        return companyRepository.findById(companyId)
                .map(companyResponseConverter::convert);
    }

    @Transactional
    @Override
    public CompanyResponse createCompany(final String userId, final CompanyRegistrationRequest companyRegistrationRequest)
            throws CompanyNotFoundException, CompanyAlreadyExistsException {

        final CompanyRequest company = companyRegistrationRequest.getCompany();
        if (!companyRepository.findByCompanyNumberOrVatNumber(company.getCompanyNumber(), company.getVatNumber()).isEmpty()) {
            log.info("Company Number [ {} ] or VAT Number [ {} ] already exists", company.getCompanyNumber(), company.getVatNumber());
            throw new CompanyAlreadyExistsException(company.getCompanyNumber(), company.getVatNumber());
        }

        if (!vatValidator.validate(company.getName(), company.getVatNumber())) {
            new BadRequestException("Invalid VAT number");
        }

        if (!companyValidator.validate(company.getName(), company.getCompanyNumber())) {
            new BadRequestException("Invalid company number");
        }

        //TODO: FCA Number integration
        CompanyBO companyBO = companyRequestConverter.convert(company);
        companyBO = companyRepository.save(companyBO);

        final CompanyResponse companyResponse = companyResponseConverter.convert(companyBO);

        final BrokerProfileBO brokerProfileBO = brokerProfileRequestConverter.convert(companyRegistrationRequest.getBrokerProfile());
        brokerProfileRepository.save(brokerProfileBO);
        publishService.sendMessage(PublishAction.COMPANY_CREATED, companyResponse);

        return companyResponse;
    }

    @Override
    public void updateCompany(final String companyId, final CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        if (companyRepository.findByName(companyRequest.getName())
                .filter(companyBO -> !companyBO.getId().equalsIgnoreCase(companyId))
                .isPresent()) {
            throw new CompanyAlreadyExistsException(companyRequest.getName());
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
        publishService.sendMessage(PublishAction.COMPANY_UPDATED, companyResponse);
    }
}
