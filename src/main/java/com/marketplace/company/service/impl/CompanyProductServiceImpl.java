package com.marketplace.company.service.impl;

import com.marketplace.company.domain.CompanyServiceBO;
import com.marketplace.company.dto.CompanyServiceResponse;
import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.CompanyServiceAlreadyExistsException;
import com.marketplace.company.exception.ServiceNotFoundException;
import com.marketplace.company.service.CompanyProductService;
import com.marketplace.company.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
class CompanyProductServiceImpl implements CompanyProductService {

  private final CompanyServiceRepository companyServiceRepository;
  private final ProductService productService;

  @Override
  public CompanyServiceResponse getCompanyServices(final String companyId) {
    LOGGER.debug("Getting services for company [ {} ]", companyId);
    List<String> services = companyServiceRepository.findByCompanyId(companyId)
        .parallelStream()
        .map(CompanyServiceBO::getServiceId)
        .collect(Collectors.toList());

    return new CompanyServiceResponse(services);
  }

  @Override
  public void addCompanyService(final String companyId, final String serviceId)
      throws CompanyServiceAlreadyExistsException, ServiceNotFoundException {

    LOGGER.debug("Adding product services to company [ {} ]", companyId);
    if (companyServiceRepository.findByCompanyIdAndServiceId(companyId, serviceId).isPresent()) {
      throw new CompanyServiceAlreadyExistsException(serviceId);
    }

    Optional<ServiceResponse> serviceOptional = productService.findByServiceId(serviceId);
    serviceOptional
        .filter(service -> service.getParentId() != null)
        .orElseThrow(() -> new ServiceNotFoundException(serviceId));

    CompanyServiceBO companyServiceBO = new CompanyServiceBO()
        .setCompanyId(companyId)
        .setServiceId(serviceId);

    companyServiceRepository.save(companyServiceBO);
    //TODO: Publish company added service
  }

  @Override
  public void removeCompanyService(final String companyId, final String serviceId) {
    LOGGER.debug("Removing service [ {} ] from the company [ {} ]", serviceId, companyId);
    companyServiceRepository.findByCompanyIdAndServiceId(companyId, serviceId)
        .ifPresent(companyServiceRepository::delete);
  }
}
