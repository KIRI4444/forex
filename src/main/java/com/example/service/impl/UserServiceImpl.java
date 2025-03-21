package com.example.service.impl;

import com.example.domain.Profile.Profile;
import com.example.domain.ProfilePhoto.ProfilePhoto;
import com.example.domain.exception.ResourceNotFoundException;
import com.example.domain.user.Role;
import com.example.domain.user.User;
import com.example.repository.ProfilePhotoRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ProfilePhotoRepository profilePhotoRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User create(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        if(!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);

        Profile profile = new Profile();
        profile.setName(null);
        profile.setSex(null);
        profile.setDescription(null);
        profile.setPhoto(null);

        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setPhoto(null);
        profilePhoto.setProfile(profile);
        profile.setPhoto(profilePhoto);
        user.setProfile(profile);
        profile.setUser(user);

        userRepository.save(user);
        profileRepository.save(profile);
        profilePhotoRepository.save(profilePhoto);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
