package com.marketplace.company.service;

import com.marketplace.company.dto.ServiceRequest;
import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.ServiceAlreadyExistsException;
import com.marketplace.company.exception.ServiceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ServiceResponse> findParentByIsActive(boolean isActive);

    List<ServiceResponse> findByParentIdAndIsActive(String parentId, boolean isActive) throws ServiceNotFoundException;

    Optional<ServiceResponse> findByServiceId(String serviceId);

    ServiceResponse addService(ServiceRequest serviceRequest) throws ServiceNotFoundException, ServiceAlreadyExistsException;

    void updateService(String serviceId, ServiceRequest serviceRequest) throws ServiceNotFoundException, ServiceAlreadyExistsException;
}
