package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.broker.profile.domain.CompanyEmployeeBO;
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
    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final CompanyResponseConverter companyResponseConverter;
    private final CompanyRequestConverter companyRequestConverter;
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
    public CompanyResponse createCompany(final String userId, final CompanyRequest companyRequest) throws CompanyNotFoundException, CompanyAlreadyExistsException {

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
        final CompanyEmployeeBO companyEmployeeBO = new CompanyEmployeeBO()
                .setCompanyId(companyBO.getId())
                .setUserId(userId)
                .setAdminPrivilege(true);

        companyEmployeeRepository.save(companyEmployeeBO);
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
