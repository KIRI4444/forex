package com.example.service.impl;

import com.example.domain.Profile.Profile;
import com.example.domain.ProfilePhoto.ProfilePhoto;
import com.example.domain.exception.ResourceNotFoundException;
import com.example.domain.user.User;
import com.example.repository.ProfilePhotoRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    @Override
    @Transactional(readOnly = true)
    public Profile getById(Long id) {
        return profileRepository.findProfileByUserId(id).
                orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getByUsername(String username) {
        return profileRepository.
                findByUsername(username).
                orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    @Override
    @Transactional
    public Profile update(Profile updatedProfile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Profile existingProfile = user.getProfile();
        if (existingProfile == null) {
            throw new ResourceNotFoundException("Profile not found for user with ID: " + userId);
        }

        existingProfile.setName(updatedProfile.getName());
        existingProfile.setAge(updatedProfile.getAge());
        existingProfile.setDescription(updatedProfile.getDescription());
        existingProfile.setSex(updatedProfile.getSex());


        return profileRepository.save(existingProfile);
    }

    @Override
    @Transactional
    public void setProfilePhoto(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        long maxSizeInBytes = 10 * 1024 * 1024;
        if (file.getSize() > maxSizeInBytes) {
            throw new IllegalArgumentException("File size the maximum allowed limit of 5 MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Uploaded file is not an image");
        }

        Profile profile = profileRepository.findProfileByUserId(userId).
                orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        byte[] photoData = file.getBytes();
        String photoBase64 = Base64.getEncoder().encodeToString(photoData);

        ProfilePhoto existingPhoto = profile.getPhoto();

        existingPhoto.setPhoto(photoBase64);
        profile.setPhoto(existingPhoto);

        profilePhotoRepository.save(existingPhoto);
    }

    @Override
    public String getProfilePhoto(Long userId) {
        Profile profile = profileRepository.findProfileByUserId(userId).
                orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profile.getPhoto().getPhoto();
    }
}
