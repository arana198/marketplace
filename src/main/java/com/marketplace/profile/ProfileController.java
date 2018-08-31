package com.marketplace.profile;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.profile.dto.UserProfileRequest;
import com.marketplace.profile.dto.UserProfileResponse;
import com.marketplace.profile.exception.UserProfileNotFoundException;
import com.marketplace.profile.service.UserProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/users/{userId}/profiles")
@PreAuthorize("@securityUtils.checkIfUserAuthorized(#userId)")
public class ProfileController {

     private final UserProfileService userProfileService;

     @RolesAllowed({UserRole.ROLE_USER})
     @GetMapping
     public ResponseEntity<UserProfileResponse> getProfile(@PathVariable final String userId)
         throws ResourceNotFoundException {
          LOGGER.info("Getting profile for user id: {}", userId);
          return userProfileService.findByUserId(userId)
              .map(ResponseEntity::ok)
              .orElseThrow(() -> new UserProfileNotFoundException(userId));
     }

     @RolesAllowed({UserRole.ROLE_USER})
     @PostMapping
     public ResponseEntity<Void> createProfile(@PathVariable final String userId,
                                               @RequestBody @Valid final UserProfileRequest userProfile,
                                               final BindingResult bindingResult)
         throws ResourceNotFoundException, ResourceAlreadyExistsException {
          LOGGER.info("Creating company for user: {}", userId);
          if (bindingResult.hasErrors()) {
               throw new BadRequestException("Invalid user company object", bindingResult);
          }

          final UserProfileResponse userProfileResponse = userProfileService.createProfile(userId, userProfile);
          final URI location = ServletUriComponentsBuilder
              .fromCurrentRequest().path("/{id}")
              .buildAndExpand(userProfileResponse.getProfileId()).toUri();

          return ResponseEntity.created(location).build();
     }

     @RolesAllowed({UserRole.ROLE_USER})
     @PutMapping(value = "/{profileId}")
     public ResponseEntity<Void> updateProfile(@PathVariable final String userId,
                                               @PathVariable final String profileId,
                                               @RequestBody @Valid final UserProfileRequest userProfile,
                                               final BindingResult bindingResult)
         throws ResourceNotFoundException, ResourceAlreadyExistsException {
          LOGGER.info("Updating company {}, for user: {}", profileId, userId);
          if (bindingResult.hasErrors()) {
               throw new BadRequestException("Invalid user company object", bindingResult);
          }

          userProfileService.updateProfile(userId, profileId, userProfile);
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
}
