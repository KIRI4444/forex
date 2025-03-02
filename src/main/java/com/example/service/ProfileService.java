package com.example.service;

import com.example.domain.Profile.Profile;

public interface ProfileService {

    Profile getById(Long id);

    Profile getByUsername(String username);

    Profile update(Profile profile, Long userId);

    Profile create(Profile profile, Long userId);
}
