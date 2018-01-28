package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.ServiceBO;
import com.marketplace.company.dto.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
class ServiceRequestConverter implements BaseConverter<ServiceRequest, ServiceBO> {

    @Override
    public ServiceBO convert(final ServiceRequest source) {
        final String name = source.getName().toUpperCase().replace(" ", "_");
        return new ServiceBO()
                .setName(name)
                .setDisplayName(source.getName())
                .setDescription(source.getDescription())
                .setActive(source.isActive());
    }
}
