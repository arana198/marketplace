package com.marketplace.user.controller;

import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserRequest.UserType;
import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Data
@Slf4j
@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResourceSupport> signup(@RequestParam UserType userType, @Valid @RequestBody final UserRequest userRequest)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        userService.createUser(userRequest, userType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
