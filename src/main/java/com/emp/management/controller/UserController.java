package com.emp.management.controller;

import com.emp.management.entity.User;
import com.emp.management.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> registerUser(@RequestBody @NotNull @Valid User user) {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Boolean> validateUser(@RequestBody @NotNull @Valid User user) {
        return new ResponseEntity<>(userService.validateUser(user), HttpStatus.OK);
    }
}
