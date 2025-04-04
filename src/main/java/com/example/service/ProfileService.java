package com.example.service;

import com.example.domain.Profile.Profile;
import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ProfileService {

    Profile getById(Long userId);

    Profile getByUsername(String username);

    Profile update(Profile updatedProfile);

    void setProfilePhoto(Long userId, MultipartFile file) throws IOException;

    String getProfilePhoto(Long userId);

    ApiResponse<?> findUsers(String query, int page, int size);
}
