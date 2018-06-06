package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.ServiceBO;
import com.marketplace.company.dto.ServiceResponse;
import org.springframework.stereotype.Service;

@Service
class ServiceResponseConverter implements BaseConverter<ServiceBO, ServiceResponse> {

     @Override
     public ServiceResponse convert(final ServiceBO source) {
          return new ServiceResponse(
              source.getId(),
              source.getDisplayName(),
              source.getDescription(),
              source.getParent() == null ? null : source.getParent().getId());
     }
}
