package com.example.web.controllers;

import com.example.domain.Profile.Profile;
import com.example.domain.response.ApiResponse;
import com.example.service.ProfileService;
import com.example.web.dto.profile.ProfileDTO;
import com.example.web.mappers.ProfileMapper;
import com.example.web.utils.Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "User profile controller", description = "Profile API")
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @GetMapping("/id/{id}")
    public ProfileDTO getProfileById(@PathVariable long id) {
        Profile profile = profileService.getById(id);
        return profileMapper.toDto(profile);
    }

    @GetMapping("/username/{username}")
    public ProfileDTO getProfileByUsername(@PathVariable String username) {
        Profile profile = profileService.getByUsername(username);
        return profileMapper.toDto(profile);
    }

    @PostMapping("/edit")
    public ProfileDTO editProfile(@RequestBody ProfileDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        Profile changedProfile = profileService.update(profile);
        return profileMapper.toDto(changedProfile);
    }

    @PostMapping("/photo")
    public ApiResponse<String> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        try {
            long userId = Util.getCurrentUserId();
            profileService.setProfilePhoto(userId, file);
            String photo = profileService.getProfilePhoto(userId);
            ApiResponse<String> response = new ApiResponse<>("Profile photo uploaded successfully", photo);
            return response;
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>("Failed to upload photo", null);
            return response;
        }
    }
}
