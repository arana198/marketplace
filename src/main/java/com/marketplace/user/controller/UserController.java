package com.marketplace.user.controller;

import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@Slf4j
@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;
}
