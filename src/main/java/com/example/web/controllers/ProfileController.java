package com.example.web.controllers;

import com.example.domain.Profile.Profile;
import com.example.service.ProfileService;
import com.example.web.dto.profile.ProfileDTO;
import com.example.web.mappers.ProfileMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ProfileDTO createProfile(@RequestBody ProfileDTO profileDTO, @RequestParam Long userId) {
        Profile profile = profileMapper.toEntity(profileDTO);
        Profile savedProfile = profileService.create(profile, userId);
        return profileMapper.toDto(savedProfile);
    }

    @PostMapping("/edit")
    public ProfileDTO editProfile(@RequestBody ProfileDTO profileDTO, @RequestParam Long userId) {
        Profile profile = profileMapper.toEntity(profileDTO);
        Profile changedProfile = profileService.update(profile, userId);
        return profileMapper.toDto(changedProfile);
    }
}
