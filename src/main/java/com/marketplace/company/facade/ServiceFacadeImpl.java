package com.marketplace.company.facade;

import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.ServiceNotFoundException;
import com.marketplace.company.service.AdviceService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
class ServiceFacadeImpl implements ServiceFacade {

    private final AdviceService adviceService;

    public List<ServiceResponse> findByParentIdAndIsActive(final String parentId, final boolean isActive) throws ServiceNotFoundException {
        List<ServiceResponse> serviceResponses;

        if (parentId == null) {
            serviceResponses = adviceService.findParentByIsActive(isActive);
        } else {
            return adviceService.findByParentIdAndIsActive(parentId, isActive);
        }

        return serviceResponses;
    }
}
