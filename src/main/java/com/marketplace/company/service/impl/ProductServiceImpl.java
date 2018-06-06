package com.marketplace.company.service.impl;

import com.marketplace.company.domain.ServiceBO;
import com.marketplace.company.dto.ServiceRequest;
import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.ServiceAlreadyExistsException;
import com.marketplace.company.exception.ServiceNotFoundException;
import com.marketplace.company.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
class ProductServiceImpl implements ProductService {

     private final ServiceRepository serviceRepository;
     private final ServiceResponseConverter serviceResponseConverter;
     private final ServiceRequestConverter serviceRequestConverter;

     @Override
     public List<ServiceResponse> findParentByIsActive(final boolean isActive) {
          return serviceRepository.findByParentAndActive(null, isActive)
              .parallelStream()
              .map(serviceResponseConverter::convert)
              .collect(Collectors.toList());
     }

     @Override
     public List<ServiceResponse> findByParentIdAndIsActive(final String parentId, final boolean isActive) throws ServiceNotFoundException {
          final ServiceBO parentBO = serviceRepository.findById(parentId)
              .orElseThrow(() -> new ServiceNotFoundException(parentId));

          return serviceRepository.findByParentAndActive(parentBO, isActive)
              .parallelStream()
              .map(serviceResponseConverter::convert)
              .collect(Collectors.toList());
     }

     @Override
     public Optional<ServiceResponse> findByServiceId(final String serviceId) {
          return serviceRepository.findById(serviceId)
              .map(serviceResponseConverter::convert);
     }

     @Override
     public ServiceResponse addService(final ServiceRequest serviceRequest) throws ServiceNotFoundException, ServiceAlreadyExistsException {

          ServiceBO parent = null;
          if (!StringUtils.isEmpty(serviceRequest.getParentServiceId())) {
               parent = serviceRepository.findById(serviceRequest.getParentServiceId())
                   .orElseThrow(() -> new ServiceNotFoundException(serviceRequest.getParentServiceId()));

          }

          this.checkIfNameIsUnique(serviceRequest.getName());
          ServiceBO serviceBO = serviceRequestConverter.convert(serviceRequest)
              .setParent(parent);

          serviceRepository.save(serviceBO);
          return serviceResponseConverter.convert(serviceBO);
     }

     @Override
     public void updateService(final String serviceId, final ServiceRequest serviceRequest) throws ServiceNotFoundException, ServiceAlreadyExistsException {
          ServiceBO oldService = serviceRepository.findById(serviceId)
              .orElseThrow(() -> new ServiceNotFoundException(serviceId));

          if (!oldService.getDisplayName().equalsIgnoreCase(serviceRequest.getName())) {
               this.checkIfNameIsUnique(serviceRequest.getName());
          }

          if (oldService.getParent() != null && !oldService.getParent().getId().equalsIgnoreCase(serviceRequest.getParentServiceId())) {
               serviceRepository.findById(serviceRequest.getParentServiceId())
                   .orElseThrow(() -> new ServiceNotFoundException(serviceRequest.getParentServiceId()));
          }

          ServiceBO newService = serviceRequestConverter.convert(serviceRequest);
          newService.update(oldService);
          serviceRepository.save(newService);
     }

     private void checkIfNameIsUnique(final String displayName) throws ServiceAlreadyExistsException {
          final String name = displayName.toUpperCase().replace(" ", "_");
          serviceRepository.findByName(name)
              .orElseThrow(() -> new ServiceAlreadyExistsException(displayName));
     }
}
