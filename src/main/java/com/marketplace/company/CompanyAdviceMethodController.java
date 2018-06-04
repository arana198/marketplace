package com.marketplace.company;

import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.CompanyAdviceMethodResponse;
import com.marketplace.company.service.CompanyAdviceMethodService;
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
@RequestMapping("/companies/{companyId}/advices")
public class CompanyAdviceMethodController {

  private final CompanyAdviceMethodService companyAdviceMethodService;

  @GetMapping
  public ResponseEntity<CompanyAdviceMethodResponse> getCompanyServices(@PathVariable final String companyId) {
    CompanyAdviceMethodResponse advices = companyAdviceMethodService.getCompanyAdvice(companyId);
    return new ResponseEntity<>(advices, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
  @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
  @PostMapping(path = "/{adviceId}")
  public ResponseEntity<Void> addCompanyService(@PathVariable final String companyId,
                                                @PathVariable final String adviceId)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {

    LOGGER.info("Adding advice method [ {} ] to the company [ {} ]", adviceId, companyId);

    companyAdviceMethodService.addCompanyAdvice(companyId, adviceId);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
  @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
  @DeleteMapping(path = "/{adviceId}")
  public ResponseEntity<Void> removeCompanyService(@PathVariable final String companyId,
                                                   @PathVariable final String adviceId) {

    LOGGER.info("Removing advice method: {} from company [ {} ]", adviceId, companyId);
    companyAdviceMethodService.removeCompanyAdvice(companyId, adviceId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
