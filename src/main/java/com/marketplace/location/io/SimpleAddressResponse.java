package com.marketplace.location.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

class SimpleAddressResponse {
  private final List<AddressComponents> address;

  @JsonCreator
  SimpleAddressResponse(@JsonProperty("results") final List<AddressComponents> address) {
    this.address = address;
  }

  public String getStreetName() {
    return address.parallelStream()
        .findAny()
        .get()
        .getStreetName();
  }

  public String getCity() {
    return address.parallelStream()
        .findAny()
        .get()
        .getCity();
  }

  public String getState() {
    return address.parallelStream()
        .findAny()
        .get()
        .getState();
  }
}

class AddressComponents {
  private final List<AddressComponent> addressComponents;

  @JsonCreator
  public AddressComponents(@JsonProperty("address_components") final List<AddressComponent> addressComponents) {
    this.addressComponents = addressComponents;
  }

  public String getStreetName() {
    return addressComponents.parallelStream()
        .filter(addressComponent -> addressComponent.getTypes().parallelStream().anyMatch(type -> type.equalsIgnoreCase("route")))
        .findAny()
        .map(AddressComponent::getLongName)
        .get();
  }

  public String getCity() {
    return addressComponents.parallelStream()
        .filter(addressComponent -> addressComponent.getTypes().parallelStream().anyMatch(type -> type.equalsIgnoreCase("postal_town")))
        .findAny()
        .map(AddressComponent::getLongName)
        .get();
  }

  public String getState() {
    return addressComponents.parallelStream()
        .filter(addressComponent -> addressComponent.getTypes().parallelStream().anyMatch(type -> type.equalsIgnoreCase("administrative_area_level_2")))
        .findAny()
        .map(AddressComponent::getLongName)
        .get();
  }
}

@Data
class AddressComponent {
  private final String longName;
  private final List<String> types;

  @JsonCreator
  AddressComponent(@JsonProperty("long_name") final String longName,
                   @JsonProperty("types") final List<String> types) {
    this.longName = longName;
    this.types = types;
  }
}