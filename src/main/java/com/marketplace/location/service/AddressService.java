package com.marketplace.location.service;

import com.marketplace.location.dto.AddressResponse;
import com.marketplace.location.exception.AddressNotFoundException;
import com.marketplace.location.exception.OutcodeNotFoundException;
import com.marketplace.location.exception.PostcodeNotFoundException;

import java.io.IOException;
import java.util.Optional;

public interface AddressService {

    Optional<AddressResponse> getAddressByPostcode(final String postcode) throws PostcodeNotFoundException, IOException, OutcodeNotFoundException, AddressNotFoundException;
}
