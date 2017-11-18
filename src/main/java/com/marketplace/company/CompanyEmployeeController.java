package com.marketplace.company;

import com.marketplace.common.annotation.IsActive;
import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.dto.CompanyEmployeeRequest;
import com.marketplace.company.dto.CompanyEmployeeResponse;
import com.marketplace.company.service.CompanyEmployeeService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;

@Data
@Slf4j
@Controller
@RequestMapping("/companies/{companyId}/employees")
public class CompanyEmployeeController {

    private final CompanyEmployeeService companyEmployeeService;

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PostMapping(path = "/invite")
    public ResponseEntity<CompanyEmployeeResponse> inviteEmployee(@PathVariable final String companyId,
                                                                  @RequestBody @Valid final CompanyEmployeeInviteRequest companyEmployeeInviteRequest,
                                                                  final BindingResult bindingResult)
            throws ResourceNotFoundException {

        log.info("Inviting employee email [ {} ] to company [ {} ]", companyEmployeeInviteRequest.getEmail(), companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company invite object", bindingResult);
        }

        companyEmployeeService.inviteEmployee(companyId, companyEmployeeInviteRequest);

        return new ResponseEntity<CompanyEmployeeResponse>(HttpStatus.CREATED);
    }

    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PostMapping
    public ResponseEntity<Void> addEmployeeToCompany(@PathVariable final String companyId,
                                                     @RequestBody @Valid final CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest,
                                                     final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Adding user [ {} ] to company: [ {} ]", AuthUser.getUserId(), companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company object", bindingResult);
        }

        final String employeeId = companyEmployeeService.addEmployeeToCompany(companyId, companyEmployeeInviteTokenRequest);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{employeeId}")
                .buildAndExpand(employeeId).toUri();

        return ResponseEntity.created(location).build();
    }

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyEmployee(#companyId, #employeeId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<Void> updateEmployeeInCompany(@PathVariable final String companyId,
                                                        @PathVariable final String employeeId,
                                                        @RequestBody @Valid final CompanyEmployeeRequest companyEmployeeRequest,
                                                        final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Updating company: {}", companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company object", bindingResult);
        }

        companyEmployeeService.updateEmployeeInCompany(companyId, employeeId, companyEmployeeRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Void> removeEmployeeFromCompany(@PathVariable final String companyId,
                                                          @PathVariable final String employeeId)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Updating company: {}", companyId);
        companyEmployeeService.removeEmployeeFromCompany(companyId, employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
