package com.marketplace.company.facade;

import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.ServiceNotFoundException;
import com.marketplace.company.service.ProductService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
class ServiceFacadeImpl implements ServiceFacade {

    private final ProductService productService;

    public List<ServiceResponse> findByParentIdAndIsActive(final String parentId, final boolean isActive) throws ServiceNotFoundException {
        List<ServiceResponse> serviceResponses;

        if (parentId == null) {
            serviceResponses = productService.findParentByIsActive(isActive);
        } else {
            return productService.findByParentIdAndIsActive(parentId, isActive);
        }

        return serviceResponses;
    }
}
