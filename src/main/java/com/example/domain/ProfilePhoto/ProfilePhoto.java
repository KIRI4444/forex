package com.example.domain.ProfilePhoto;

import com.example.domain.Profile.Profile;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profile_photos")
@Data
public class ProfilePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private Profile profile;

    private String photo;

}
