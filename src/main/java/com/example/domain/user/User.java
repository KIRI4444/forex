package com.example.domain.user;

import com.example.domain.Message.Message;
import com.example.domain.Profile.Profile;
import com.example.domain.chat.Chat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "forex")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;

    @Transient
    private String passwordConfirmation;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;

    @JsonIgnore
    @OneToMany(mappedBy = "firstUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Chat> chatsAsFirstUser = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "secondUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Chat> chatsAsSecondUser = new HashSet<>();

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Message> sentMessages = new HashSet<>();
}
