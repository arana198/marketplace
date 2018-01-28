package com.marketplace.location.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

import java.util.List;

@Data
public class AddressResponse extends BaseResponseDomain {
    private final List<String> address;
    private final String city;
    private final String state;
    private final String postcode;
}
