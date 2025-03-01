package com.example.domain.Profile;

import com.example.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "profiles")
@Data
public class Profile implements Serializable {

    @Id
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private int age;
    private String description;
    private int sex;

}
