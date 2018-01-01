package com.marketplace.company;

import com.marketplace.common.annotation.IsActive;
import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.BrokerProfileRequest;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.exception.BrokerNotFoundException;
import com.marketplace.company.service.BrokerService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@Data
@Slf4j
@Controller
@RequestMapping("/companies/{companyId}/brokers")
public class BrokerController {

    private final BrokerService brokerService;

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PostMapping(path = "/invite")
    public ResponseEntity<BrokerProfileResponse> inviteEmployee(@PathVariable final String companyId,
                                                                @RequestBody @Valid final CompanyEmployeeInviteRequest companyEmployeeInviteRequest,
                                                                final BindingResult bindingResult)
            throws ResourceNotFoundException {

        log.info("Inviting broker email [ {} ] to company [ {} ]", companyEmployeeInviteRequest.getEmail(), companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company invite object", bindingResult);
        }

        brokerService.inviteEmployee(companyId, companyEmployeeInviteRequest);

        return new ResponseEntity<BrokerProfileResponse>(HttpStatus.CREATED);
    }

    @PreAuthorize("@securityUtils.isCompanyEmployee(#companyId, #brokerId)")
    @RolesAllowed({UserRole.ROLE_BROKER})
    @GetMapping(path = "/{brokerId}")
    public ResponseEntity<BrokerProfileResponse> getBrokerProfile(@PathVariable final String companyId,
                                                                  @PathVariable final String brokerId)
            throws ResourceNotFoundException {

        BrokerProfileResponse brokerProfileResponse = brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerId)
                .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerId));

        return new ResponseEntity<>(brokerProfileResponse, HttpStatus.OK);
    }

    @RolesAllowed({UserRole.ROLE_BROKER})
    @PostMapping
    public ResponseEntity<Void> addEmployeeToCompany(@PathVariable final String companyId,
                                                     @RequestBody @Valid final CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest,
                                                     final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Adding broker [ {} ] to company: [ {} ]", AuthUser.getUserId(), companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company object", bindingResult);
        }

        final BrokerProfileResponse brokerProfileResponse = brokerService.addBrokerToCompany(AuthUser.getUserId(), companyId, companyEmployeeInviteTokenRequest);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{brokerId}")
                .buildAndExpand(brokerProfileResponse.getBrokerProfileId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RolesAllowed({UserRole.ROLE_BROKER})
    @PutMapping(path = "/{brokerId}")
    public ResponseEntity<Void> updateBrokerInCompany(@PathVariable final String companyId,
                                                      @PathVariable final String brokerId,
                                                      @RequestBody @Valid final BrokerProfileRequest brokerProfileRequest,
                                                      final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Updating broker: {} for comapny: ", brokerId, companyId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid company object", bindingResult);
        }

        brokerService.updateBrokerProfile(AuthUser.getUserId(), companyId, brokerId, brokerProfileRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @DeleteMapping(path = "/{brokerId}")
    public ResponseEntity<Void> removeBrokerFromCompany(@PathVariable final String companyId,
                                                        @PathVariable final String brokerId)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Removing broker {} from company: {}", brokerId, companyId);
        brokerService.removeBrokerFromCompany(companyId, brokerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @DeleteMapping(path = "/{brokerId}/admins")
    public ResponseEntity<Void> removeAdminBrokerFromCompany(@PathVariable final String companyId,
                                                             @PathVariable final String brokerId)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Removing admin broker {} from company: {}", brokerId, companyId);
        brokerService.removeAdminBrokerFromCompany(companyId, brokerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @IsActive
    @PreAuthorize("@securityUtils.isCompanyAdmin(#companyId)")
    @RolesAllowed({UserRole.ROLE_COMPANY_ADMIN})
    @PostMapping(path = "/{brokerId}/admins")
    public ResponseEntity<Void> addAdminBrokerForCompany(@PathVariable final String companyId,
                                                         @PathVariable final String brokerId)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Add admin broker {} for company: {}", brokerId, companyId);
        brokerService.addAdminBrokerForCompany(companyId, brokerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed({UserRole.ROLE_BROKER})
    @PutMapping(path = "/{brokerId}/profileimages", consumes = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<BrokerProfileResponse> addOrUpdateProfileImage(@PathVariable final String companyId,
                                                                         @PathVariable final String brokerId,
                                                                         @RequestPart(name = "file") final MultipartFile multipartFile,
                                                                         final BindingResult bindingResult)
            throws ResourceNotFoundException, IOException {

        log.info("Updating broker: {} for company: ", brokerId, companyId);

        BrokerProfileResponse brokerProfileResponse = brokerService.addOrUpdateImage(AuthUser.getUserId(), companyId, brokerId, multipartFile);
        return new ResponseEntity<>(brokerProfileResponse, HttpStatus.OK);
    }
}
