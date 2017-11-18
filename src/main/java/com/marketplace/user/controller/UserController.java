package com.marketplace.user.controller;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.user.dto.EmailVerificationRequest;
import com.marketplace.user.dto.UpdatePasswordRequest;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserRequest.UserType;
import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Data
@Slf4j
@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> signup(@RequestParam UserType userType,
                                       @Valid @RequestBody final UserRequest userRequest,
                                       final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Signup user: {}", userRequest.getEmail());
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid user request object", bindingResult);
        }

        userService.createUser(userRequest, userType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("@securityUtils.checkIfUserAuthorized(#userId)")
    @RolesAllowed({UserRole.ROLE_BROKER})
    @PutMapping(path = "/{userId}/passwords")
    public ResponseEntity<Void> updatePassword(@PathVariable String userId,
                                               @Valid @RequestBody final UpdatePasswordRequest updatePasswordRequest,
                                               final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Update password for user: {}", userId);
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid update password request object", bindingResult);
        }

        userService.updatePassword(userId, updatePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/verifyemail")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody final EmailVerificationRequest emailVerificationRequest,
                                            final BindingResult bindingResult)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        log.info("Verify email for token: {}", emailVerificationRequest.getToken());
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Invalid update password request object", bindingResult);
        }

        userService.verifyEmail(emailVerificationRequest);
        return ResponseEntity.ok().build();
    }
}
