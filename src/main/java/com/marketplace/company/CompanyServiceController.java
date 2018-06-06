package com.marketplace.company;

import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.CompanyServiceResponse;
import com.marketplace.company.service.CompanyProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/companies/{companyId}/services")
public class CompanyServiceController {

     private final CompanyProductService companyProductService;

     @GetMapping
     public ResponseEntity<CompanyServiceResponse> getCompanyServices(@PathVariable final String companyId) {
          CompanyServiceResponse services = companyProductService.getCompanyServices(companyId);
          return new ResponseEntity<>(services, HttpStatus.OK);
     }

     @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
     @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
     @PostMapping(path = "/{serviceId}")
     public ResponseEntity<Void> addCompanyService(@PathVariable final String companyId,
                                                   @PathVariable final String serviceId)
         throws ResourceNotFoundException, ResourceAlreadyExistsException {

          LOGGER.info("Adding service [ {} ] to the company [ {} ]", serviceId, companyId);

          companyProductService.addCompanyService(companyId, serviceId);

          return new ResponseEntity<>(HttpStatus.CREATED);
     }

     @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
     @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
     @DeleteMapping(path = "/{serviceId}")
     public ResponseEntity<Void> removeCompanyService(@PathVariable final String companyId,
                                                      @PathVariable final String serviceId) {

          LOGGER.info("Removing service: {} from company [ {} ]", serviceId, companyId);
          companyProductService.removeCompanyService(companyId, serviceId);

          return new ResponseEntity<>(HttpStatus.OK);
     }
}
