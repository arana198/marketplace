package com.marketplace.location.service.impl;

import com.marketplace.location.domain.AddressBO;
import com.marketplace.location.dto.AddressResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class AddressConverter {

     public AddressResponse convert(final AddressBO addressBO) {
          return new AddressResponse(
              addressBO.getId(),
              addressBO.getAddress(),
              null,
              addressBO.getCity().getName(),
              addressBO.getState().getName(),
              addressBO.getPostcode().getPostcode());
     }

     public List<AddressResponse> convert(final List<AddressBO> addressBOList) {
          return addressBOList.parallelStream()
              .map(this::convert)
              .collect(Collectors.toList());
     }
}
