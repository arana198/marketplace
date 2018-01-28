package com.marketplace.location;

import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.location.dto.AddressResponse;
import com.marketplace.location.exception.AddressNotFoundException;
import com.marketplace.location.service.AddressService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Data
@Slf4j
@Controller
@RequestMapping("/address/{postcode}")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<AddressResponse> getAddress(@PathVariable final String postcode)
            throws ResourceNotFoundException, IOException {

        log.info("Get address for location [ {} ]", postcode);

        AddressResponse addressResponse = addressService.getAddressByPostcode(postcode)
                .orElseThrow(() -> new AddressNotFoundException(postcode));

        return ResponseEntity.ok(addressResponse);
    }
}
