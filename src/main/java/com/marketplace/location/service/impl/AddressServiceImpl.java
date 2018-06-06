package com.marketplace.location.service.impl;

import com.marketplace.location.domain.AddressBO;
import com.marketplace.location.domain.CityBO;
import com.marketplace.location.domain.OutcodeBO;
import com.marketplace.location.domain.PostcodeBO;
import com.marketplace.location.domain.StateBO;
import com.marketplace.location.dto.AddressResponse;
import com.marketplace.location.exception.AddressNotFoundException;
import com.marketplace.location.exception.OutcodeNotFoundException;
import com.marketplace.location.exception.PostcodeNotFoundException;
import com.marketplace.location.io.AddressFinderService;
import com.marketplace.location.io.dto.Address;
import com.marketplace.location.io.dto.Outcode;
import com.marketplace.location.io.dto.Postcode;
import com.marketplace.location.service.AddressService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
class AddressServiceImpl implements AddressService {

     private final PostcodeRepository postcodeRepository;
     private final AddressRepository addressRepository;
     private final CityRepository cityRepository;
     private final StateRepository stateRepository;
     private final OutcodeRepository outcodeRepository;
     private final AddressFinderService addressFinderService;
     private final AddressConverter addressConverter;

     @Override
     public List<AddressResponse> getAddressByPostcode(final String postcode)
         throws PostcodeNotFoundException, IOException, OutcodeNotFoundException, AddressNotFoundException {

          LOGGER.info("Getting address for location {}", postcode);
          return addressConverter.convert(this.getOrCreateAddressBO(postcode.replaceAll(" ", "")));
     }

     private List<AddressBO> getOrCreateAddressBO(final String postcode)
         throws PostcodeNotFoundException, IOException, OutcodeNotFoundException, AddressNotFoundException {

          List<AddressBO> addressList = addressRepository.findByPostcode(postcode);

          if (addressList.isEmpty()) {
               final Address address = addressFinderService.getAddressByPostcode(postcode);
               PostcodeBO postcodeBO = this.getOrCreatePostcodeBO(address);

               if (!address.getHouseNumber().isEmpty()) {
                    addressList = address.getHouseNumber()
                        .parallelStream()
                        .map(houseNumber -> {
                             final OutcodeBO outcodeBO = postcodeBO.getOutcode();
                             return new AddressBO()
                                 .setAddress(houseNumber)
                                 .setPostcode(postcodeBO)
                                 .setCity(outcodeBO.getCity())
                                 .setState(outcodeBO.getCity().getState());
                        })
                        .collect(Collectors.toList());

                    addressRepository.saveAll(addressList);
               }
          }

          return addressList;
     }

     private StateBO getOrCreateStateBO(final Address address) {
          return stateRepository.findByName(address.getState())
              .orElseGet(() -> {
                   StateBO stateBO = new StateBO()
                       .setName(address.getState());

                   stateRepository.save(stateBO);
                   return stateBO;
              });
     }

     private CityBO getOrCreateCityBO(final Address address) {
          return cityRepository.findByName(address.getCity())
              .orElseGet(() -> {
                   CityBO cityBO = new CityBO()
                       .setState(this.getOrCreateStateBO(address))
                       .setName(address.getCity());

                   cityRepository.save(cityBO);
                   return cityBO;
              });
     }

     private PostcodeBO getOrCreatePostcodeBO(final Address address) {
          final Postcode postcode = address.getPostcode();
          final String postcodeStr = postcode.getPostcode().replaceAll(" ", "");
          return postcodeRepository.findByPostcode(postcodeStr)
              .orElseGet(() -> {
                   Coordinate coordinate = new Coordinate(postcode.getLatitude(), postcode.getLongitude());
                   PostcodeBO postcodeBO = new PostcodeBO()
                       .setPostcode(postcodeStr)
                       .setActive(true)
                       .setOutcode(this.getOrCreateOutcodeBO(address))
                       .setCoordinates(new GeometryFactory().createPoint(coordinate));

                   postcodeRepository.save(postcodeBO);
                   return postcodeBO;
              });
     }

     private OutcodeBO getOrCreateOutcodeBO(final Address address) {
          final Outcode outcode = address.getPostcode().getOutcode();
          return outcodeRepository.findByOutcode(outcode.getOutcode())
              .orElseGet(() -> {
                   Coordinate coordinate = new Coordinate(outcode.getLatitude(), outcode.getLongitude());
                   OutcodeBO outcodeBO = new OutcodeBO()
                       .setActive(true)
                       .setOutcode(outcode.getOutcode())
                       .setCity(this.getOrCreateCityBO(address))
                       .setCoordinates(new GeometryFactory().createPoint(coordinate));

                   outcodeRepository.save(outcodeBO);
                   return outcodeBO;
              });
     }

}
