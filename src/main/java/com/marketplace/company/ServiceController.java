package com.marketplace.company;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.ServiceRequest;
import com.marketplace.company.dto.ServiceResponse;
import com.marketplace.company.exception.ServiceNotFoundException;
import com.marketplace.company.facade.ServiceFacade;
import com.marketplace.company.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/services")
public class ServiceController {

  private final ProductService productService;
  private final ServiceFacade serviceFacade;

  @GetMapping
  public ResponseEntity<List<ServiceResponse>> getActiveServices(@RequestParam(name = "parentId", required = false) final String parentId)
      throws ServiceNotFoundException {

    List<ServiceResponse> services = serviceFacade.findByParentIdAndIsActive(parentId, true);
    return new ResponseEntity<>(services, HttpStatus.OK);
  }

  @RolesAllowed({UserRole.ROLE_ADMIN})
  @PostMapping
  public ResponseEntity<Void> addService(@RequestBody @Valid final ServiceRequest serviceRequest,
                                         final BindingResult bindingResult)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {

    LOGGER.info("Adding service [ {} ]", serviceRequest.getName());
    if (bindingResult.hasErrors()) {
      throw new BadRequestException("Invalid service object", bindingResult);
    }

    final ServiceResponse serviceResponse = productService.addService(serviceRequest);
    final URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{serviceId}")
        .buildAndExpand(serviceResponse.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @RolesAllowed({UserRole.ROLE_ADMIN})
  @PutMapping(path = "/{serviceId}")
  public ResponseEntity<Void> updateService(@PathVariable final String serviceId,
                                            @RequestBody @Valid final ServiceRequest serviceRequest,
                                            final BindingResult bindingResult)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {

    LOGGER.info("Updating service: {}", serviceId);
    if (bindingResult.hasErrors()) {
      throw new BadRequestException("Invalid service object", bindingResult);
    }

    productService.updateService(serviceId, serviceRequest);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
