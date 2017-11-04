package com.marketplace.broker.profile;

import com.marketplace.broker.profile.dto.CompanyRequest;
import com.marketplace.broker.profile.dto.CompanyResponse;
import com.marketplace.broker.profile.exception.CompanyNotFoundException;
import com.marketplace.broker.profile.service.CompanyService;
import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.utils.PagedResourceConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Data
@Slf4j
@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @RolesAllowed({UserRole.ROLE_ADMIN})
    @GetMapping
    public PagedResources<CompanyResponse> getProfile(@RequestParam(value = "name", required = false) final String companyName,
                                                      final Pageable pageable) {
        final Page<CompanyResponse> companyResponses = companyService.findAll(companyName, pageable);
        return PagedResourceConverter.convert(companyResponses);
    }

    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @GetMapping(value = "/{companyId}")
    public ResponseEntity<CompanyResponse> getProfile(@PathVariable final String companyId)
            throws ResourceNotFoundException {
        log.info("Getting company for id: {}", companyId);
        return companyService.findById(companyId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));
    }

    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PostMapping
    public ResponseEntity<Void> createCompany(@RequestBody @Valid final CompanyRequest companyRequest,
                                              final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Creating company: {}", companyRequest.getName());
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company object", bindingResult);
        }

        final CompanyResponse companyResponse = companyService.createCompany(companyRequest);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(companyResponse.getCompanyId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PutMapping(value = "/{companyId}")
    public ResponseEntity<Void> updateCompany(@PathVariable final String companyId,
                                              @RequestBody @Valid final CompanyRequest companyRequest,
                                              final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Updating company: {}", companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company object", bindingResult);
        }

        companyService.updateCompany(companyId, companyRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @DeleteMapping(value = "/{companyId}")
    public ResponseEntity<Void> inactivateCompany(@PathVariable final String companyId)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Inactivating company: {}", companyId);
        companyService.inactivateCompany(companyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
