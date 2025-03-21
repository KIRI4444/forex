package com.example.repository;

import com.example.domain.ProfilePhoto.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
}
