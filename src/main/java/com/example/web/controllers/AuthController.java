package com.example.web.controllers;


import com.example.domain.user.User;
import com.example.service.AuthService;
import com.example.service.UserService;
import com.example.web.dto.auth.JwtRequest;
import com.example.web.dto.auth.JwtResponse;
import com.example.web.dto.auth.RefreshRequest;
import com.example.web.dto.user.UserDto;
import com.example.web.dto.validation.OnCreate;
import com.example.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        return authService.refresh(refreshToken);
    }

}
