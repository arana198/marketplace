package com.marketplace.location.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.location.exception.AddressNotFoundException;
import com.marketplace.location.exception.OutcodeNotFoundException;
import com.marketplace.location.exception.PostcodeNotFoundException;
import com.marketplace.location.io.dto.Address;
import com.marketplace.location.io.dto.Outcode;
import com.marketplace.location.io.dto.Postcode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class AddressFinderService {

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();
  private static final String ADDRESS_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=false";
  private static final String POSTCODE_URL = "https://api.postcodes.io/postcodes/%s";
  private static final String OUTCODE_URL = "https://api.postcodes.io/outcodes/%s";
  private static final String STREET_ADDRESS_URL = "https://www.hopewiser.com/demo/find-address.php?q=%s";

  public Address getAddressByPostcode(final String postcode)
      throws PostcodeNotFoundException, OutcodeNotFoundException, IOException, AddressNotFoundException {

    LOGGER.debug("Getting address for location {}", postcode);
    SimpleAddressResponse simpleAddressResponse = REST_TEMPLATE.getForObject(String.format(ADDRESS_URL, postcode), SimpleAddressResponse.class, new HashMap<>());
    return Address.builder()
        .houseNumber(this.getHouseNumber(postcode))
        .streetName(simpleAddressResponse.getStreetName())
        .city(simpleAddressResponse.getCity())
        .state(simpleAddressResponse.getState())
        .postcode(this.getPostcodeDetails(postcode))
        .build();
  }

  private List<String> getHouseNumber(final String postcode) throws IOException, AddressNotFoundException {
    LOGGER.debug("Getting address for location {}", postcode);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Cookie", "_ga=GA1.2.542764837.1516538164; _gid=GA1.2.1903034621.1516538164; wow.anonymousId=01b837bc-bc00-4aea-8183-2c740cf2250d; " +
        "wow.schedule=wowTracking_C; wow.session=01b837bc-bc00-4aea-8183-2c740cf2250d; wow.utmvalues=; gwcc=%7B%22fallback%22%3A%2201619242800%22%2C%22clabel%22%3A%22qpbCCI" +
        "DYsm4Q_aKGzgM%22%2C%22backoff%22%3A86400%2C%22backoff_expires%22%3A1516624563%7D; _gat=1; freeaddressd=2015-01-01; freeaddressc=0");

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    String response = REST_TEMPLATE.exchange(String.format(STREET_ADDRESS_URL, postcode), HttpMethod.GET, entity, String.class).getBody();
    HouseNumberResponse houseNumberResponse = new ObjectMapper().readValue(response, HouseNumberResponse.class);

    if (houseNumberResponse.getStatus().equalsIgnoreCase("OK")) {
      return houseNumberResponse.getHouseNumber();
    }

    throw new AddressNotFoundException(postcode);
  }

  private Postcode getPostcodeDetails(final String postcode) throws PostcodeNotFoundException, OutcodeNotFoundException {
    try {
      PostcodeResponse postcodeResponse = REST_TEMPLATE.getForObject(String.format(POSTCODE_URL, postcode), PostcodeResponse.class, new HashMap<>());
      return Postcode.builder()
          .postcode(postcodeResponse.getPostcode())
          .outcode(this.getOutcodeDetails(postcodeResponse.getOutcode()))
          .latitude(postcodeResponse.getLatitude())
          .longitude(postcodeResponse.getLongitude())
          .build();

    } catch (HttpClientErrorException ex) {
      if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
        LOGGER.info("Could not find location {}", postcode);
        throw new PostcodeNotFoundException(postcode);
      }

      throw ex;
    }
  }

  private Outcode getOutcodeDetails(final String outcode) throws OutcodeNotFoundException {
    try {
      PostcodeResponse outcodeResponse = REST_TEMPLATE.getForObject(String.format(OUTCODE_URL, outcode), PostcodeResponse.class, new HashMap<>());
      return Outcode.builder()
          .outcode(outcodeResponse.getOutcode())
          .latitude(outcodeResponse.getLatitude())
          .longitude(outcodeResponse.getLongitude())
          .build();

    } catch (HttpClientErrorException ex) {
      if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
        LOGGER.info("Could not find outcode {}", outcode);
        throw new OutcodeNotFoundException(outcode);
      }

      throw ex;
    }
  }
}
