package com.example.web.controllers;


import com.example.domain.response.ApiResponse;
import com.example.domain.user.User;
import com.example.service.ProfileService;
import com.example.service.UserService;
import com.example.web.dto.user.UserDto;
import com.example.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")

public class UserController {

    private final UserService userService;
    private final ProfileService profileService;

    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @GetMapping("/search")
    public ApiResponse<?> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return profileService.findUsers(query, page, size);
    }
}
