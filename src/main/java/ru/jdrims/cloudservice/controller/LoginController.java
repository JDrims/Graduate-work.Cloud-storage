package ru.jdrims.cloudservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jdrims.cloudservice.dto.LoginInRequest;
import ru.jdrims.cloudservice.dto.LoginInResponse;
import ru.jdrims.cloudservice.service.AuthorizationService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthorizationService authorizationService;

    public LoginController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public LoginInResponse login(@Valid @RequestBody LoginInRequest loginInRequest) {
        return authorizationService.login(loginInRequest);
    }
}