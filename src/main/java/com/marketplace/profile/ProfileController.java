package com.marketplace.profile;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.profile.dto.UserProfileRequest;
import com.marketplace.profile.dto.UserProfileResponse;
import com.marketplace.profile.exception.UserProfileNotFoundException;
import com.marketplace.profile.service.UserProfileService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;

@Data
@Slf4j
@Controller
@RequestMapping("/users/{userId}/profiles")
public class ProfileController {

    private final UserProfileService userProfileService;

    @RolesAllowed({UserRole.ROLE_USER})
    @RequestMapping(value = "/{profileId}", method = RequestMethod.GET)
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable final String userId,
                                                          @PathVariable final String profileId)
            throws ResourceNotFoundException {
        log.info("Getting profile for id: {}", profileId);
        return userProfileService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserProfileNotFoundException(userId, profileId));
    }

    @RolesAllowed({UserRole.ROLE_USER})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createProfile(@PathVariable final String userId,
                                              @RequestBody @Valid final UserProfileRequest userProfile,
                                              final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Creating profile for user: {}", userId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid user profile object", bindingResult);
        }

        final UserProfileResponse userProfileResponse = userProfileService.createProfile(userId, userProfile);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(userProfileResponse.getProfileId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RolesAllowed({UserRole.ROLE_USER})
    @RequestMapping(value = "/{profileId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateProfile(@PathVariable final String userId,
                                              @PathVariable final String profileId,
                                              @RequestBody @Valid final UserProfileRequest userProfile,
                                              final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        log.info("Updating profile {}, for user: {}", profileId, userId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid user profile object", bindingResult);
        }

        userProfileService.updateProfile(userId, profileId, userProfile);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
