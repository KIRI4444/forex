package com.example.repository;

import com.example.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query(value = """
            select *
            from users u
            where u.id=:id
            """, nativeQuery = true)
    Optional<User> getUserById(@Param("id") Long id);
}
