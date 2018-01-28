package com.marketplace.location.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class HouseNumberResponse {
    private final String status;
    private final HouseNumberList result;

    @JsonCreator
    HouseNumberResponse(@JsonProperty("Status") final String status,
                        @JsonProperty("Results") final HouseNumberList result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return this.status;
    }

    public List<String> getHouseNumber() {
        return this.result.getAddress()
                .parallelStream()
                .map(HouseNumberData::getAddress)
                .collect(Collectors.toList());
    }
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class HouseNumberList {
    private final List<HouseNumberData> address;

    @JsonCreator
    public HouseNumberList(@JsonProperty("Items") final List<HouseNumberData> address) {
        this.address = address;
    }
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class HouseNumberData {
    private final String address;

    @JsonCreator
    public HouseNumberData(@JsonProperty("ItemText") final String address) {
        String tempAddress = address.indexOf("  ") == 0 ? address.substring(2, address.length()) : address;

        if (tempAddress.indexOf(",") != -1) {
            this.address = tempAddress.substring(0, address.indexOf(","));
        } else {
            this.address = tempAddress;
        }
    }
}