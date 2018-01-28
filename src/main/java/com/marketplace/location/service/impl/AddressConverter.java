package com.marketplace.location.service.impl;

import com.marketplace.location.domain.AddressBO;
import com.marketplace.location.dto.AddressResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class AddressConverter {

    public Optional<AddressResponse> convert(final List<AddressBO> addressBOList) {
        return addressBOList.parallelStream()
                .findAny()
                .map(addressBO -> {
                    final String city = addressBO.getCity().getName();
                    final String state = addressBO.getState().getName();
                    final String postcode = addressBO.getPostcode().getPostcode();

                    final List<String> addressList = addressBOList.parallelStream()
                            .map(AddressBO::getAddress)
                            .map(address -> {
                                StringBuilder stringBuilder = new StringBuilder(address);
                                stringBuilder.append(", ");
                                stringBuilder.append(city);
                                stringBuilder.append(", ");
                                stringBuilder.append(state);
                                stringBuilder.append(", ");
                                stringBuilder.append(postcode);
                                return stringBuilder.toString();
                            })
                            .collect(Collectors.toList());

                    return new AddressResponse(addressList,
                            city,
                            state,
                            postcode);
                });
    }
}
