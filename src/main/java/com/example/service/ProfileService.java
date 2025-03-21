package com.example.service;

import com.example.domain.Profile.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {

    Profile getById(Long userId);

    Profile getByUsername(String username);

    Profile update(Profile profile, Long userId);

    void setProfilePhoto(Long userId, MultipartFile file) throws IOException;

    String getProfilePhoto(Long userId);
}
