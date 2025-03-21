package com.example.repository;

import com.example.domain.Profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query(value = """
        SELECT p.*, pp.photo
        FROM profiles p 
        JOIN users u ON p.user_id = u.id
        JOIN profile_photos pp ON pp.profile_id = p.id
        WHERE u.username = :username
        """, nativeQuery = true)
    Optional<Profile> findByUsername(@Param("username") String username);

    @Query(value = """
            SELECT p.*, pp.photo
            FROM profiles p
            JOIN profile_photos pp ON pp.profile_id = p.id
            where p.user_id = :userId
            """, nativeQuery = true)
    Optional<Profile> findProfileByUserId(@Param("userId") Long userId);
}
