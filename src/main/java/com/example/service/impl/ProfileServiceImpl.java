package com.example.service.impl;

import com.example.domain.Profile.Profile;
import com.example.domain.exception.ResourceNotFoundException;
import com.example.domain.user.User;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public Profile getById(Long id) {
        return profileRepository.findById(id).
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
    public Profile create(Profile profile, Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found"));
        profile.setUser(user);
        user.setProfile(profile);

        return profileRepository.save(profile);
    }
}
