package com.marketplace.company;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.CompanyRegistrationRequest;
import com.marketplace.company.dto.CompanyRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.service.CompanyService;
import com.marketplace.company.service.CompanyValidatorService;
import com.marketplace.utils.PagedResourceConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/companies")
public class CompanyController {

  private final CompanyService companyService;
  private final CompanyValidatorService companyValidatorService;

  @RolesAllowed({UserRole.ROLE_ADMIN})
  @GetMapping
  public PagedResources<CompanyResponse> getCompanies(@RequestParam(value = "name", required = false) final String companyName,
                                                      final Pageable pageable) {

    final Page<CompanyResponse> companyResponses = companyService.findAll(companyName, pageable);
    return PagedResourceConverter.convert(companyResponses);
  }

  @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
  @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
  @GetMapping(value = "/{companyId}")
  public ResponseEntity<CompanyResponse> getCompany(@PathVariable final String companyId)
      throws ResourceNotFoundException {

    LOGGER.info("Getting company for id: {}", companyId);
    return companyService.findById(companyId)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new CompanyNotFoundException(companyId));
  }

  @PreAuthorize("@securityUtils.isCompanyAdmin(null)")
  @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
  @PostMapping
  public ResponseEntity<Void> createCompany(@RequestBody @Valid final CompanyRegistrationRequest companyRegistrationRequest,
                                            final BindingResult bindingResult)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {

    if (bindingResult.hasErrors()) {
      throw new BadRequestException("Invalid company object", bindingResult);
    }

    LOGGER.info("Creating company: {}", companyRegistrationRequest.getCompany().getName());
    final CompanyResponse companyResponse = companyService.createCompany(AuthUser.getUserId(), companyRegistrationRequest);
    final URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{companyId}")
        .buildAndExpand(companyResponse.getCompanyId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
  @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
  @PutMapping(value = "/{companyId}")
  public ResponseEntity<Void> updateCompany(@PathVariable final String companyId,
                                            @RequestBody @Valid final CompanyRequest companyRequest,
                                            final BindingResult bindingResult)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {

    LOGGER.info("Updating company: {}", companyId);
    if (bindingResult.hasErrors()) {
      throw new BadRequestException("Invalid company object", bindingResult);
    }

    companyService.updateCompany(companyId, companyRequest);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RolesAllowed({UserRole.ROLE_ADMIN})
  @PutMapping(value = "/{companyId}/fcaNumber/verify")
  public ResponseEntity<Void> verifyCompany(@PathVariable final String companyId) {

    companyValidatorService.fcaNumberVerified(companyId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RolesAllowed({UserRole.ROLE_ADMIN})
  @DeleteMapping(value = "/{companyId}/fcaNumber/unverify")
  public ResponseEntity<Void> unverifyCompany(@PathVariable final String companyId) throws CompanyNotFoundException {

    companyValidatorService.fcaNumberUnverified(companyId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
