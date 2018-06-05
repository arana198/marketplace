package com.marketplace.company.facade;

import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.ServiceNotFoundException;

import java.util.List;

public interface ServiceFacade {

  List<ServiceResponse> findByParentIdAndIsActive(String parentId, boolean isActive) throws ServiceNotFoundException;
}
