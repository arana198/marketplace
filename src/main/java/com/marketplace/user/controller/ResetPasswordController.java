package com.marketplace.user.controller;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.user.dto.ForgottenPasswordRequest;
import com.marketplace.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/resetpassword")
public class ResetPasswordController {

     private final UserService userService;

     @PostMapping
     public ResponseEntity<ResourceSupport> createResetPasswordToken(@RequestParam final String email)
         throws ResourceNotFoundException {

          LOGGER.info("Creating reset password for user: {}", email);
          userService.resetPassword(email);
          return new ResponseEntity<ResourceSupport>(HttpStatus.CREATED);
     }

     @PutMapping
     public ResponseEntity<ResourceSupport> resetpassword(@Valid @RequestBody final ForgottenPasswordRequest forgottenPasswordRequest,
                                                          final BindingResult bindingResult)
         throws ResourceNotFoundException {

          LOGGER.info("Resetting password for user: {}", forgottenPasswordRequest.getEmail());
          if (bindingResult.hasErrors()) {
               throw new BadRequestException("Invalid forgot password object", bindingResult);
          }

          userService.resetPassword(forgottenPasswordRequest);
          return ResponseEntity.ok().build();
     }
}
