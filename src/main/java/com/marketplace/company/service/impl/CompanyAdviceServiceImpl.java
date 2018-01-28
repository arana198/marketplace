package com.marketplace.company.service.impl;

import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.company.domain.CompanyServiceBO;
import com.marketplace.company.dto.CompanyServiceResponse;
import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.CompanyServiceAlreadyExistsException;
import com.marketplace.company.exception.ServiceNotFoundException;
import com.marketplace.company.service.AdviceService;
import com.marketplace.company.service.CompanyAdviceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@Service
class CompanyAdviceServiceImpl implements CompanyAdviceService {

    private final CompanyServiceRepository companyServiceRepository;
    private final AdviceService adviceService;

    @Override
    public CompanyServiceResponse getCompanyServices(final String companyId) {
        log.debug("Getting services for company [ {} ]", companyId);
        List<String> services = companyServiceRepository.findByCompanyId(companyId)
                .parallelStream()
                .map(CompanyServiceBO::getServiceId)
                .collect(Collectors.toList());

        return new CompanyServiceResponse(services);
    }

    @Override
    public void addCompanyServices(final String companyId, final String serviceId)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.debug("Adding services to company [ {} ]", companyId);
        if (companyServiceRepository.findByCompanyIdAndServiceId(companyId, serviceId).isPresent()) {
            throw new CompanyServiceAlreadyExistsException(serviceId);
        }

        Optional<ServiceResponse> serviceOptional = adviceService.findByServiceId(serviceId);
        serviceOptional
                .filter(service -> service.getParentId() != null)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        CompanyServiceBO companyServiceBO = new CompanyServiceBO()
                .setCompanyId(companyId)
                .setServiceId(serviceId);

        companyServiceRepository.save(companyServiceBO);
    }

    @Override
    public void removeCompanyServices(final String companyId, final String serviceId) {
        log.debug("Removing service [ {} ] from the company [ {} ]", serviceId, companyId);
        companyServiceRepository.findByCompanyIdAndServiceId(companyId, serviceId)
                .ifPresent(companyServiceRepository::delete);
    }
}
