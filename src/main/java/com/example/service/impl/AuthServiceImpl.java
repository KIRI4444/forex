package com.example.service.impl;

import com.example.domain.GoogleAuth.GoogleAuthUser;
import com.example.domain.Profile.Profile;
import com.example.domain.ProfilePhoto;
import com.example.domain.user.Role;
import com.example.domain.user.User;
import com.example.repository.ProfilePhotoRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.UserService;
import com.example.web.dto.auth.JwtRequest;
import com.example.web.dto.auth.JwtResponse;
import com.example.web.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Authentication error", e);
        }
        User user = userService.getByUsername(loginRequest.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

    @Override
    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        System.out.println("idTokenString: " + idTokenString);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.gson.GsonFactory()
        )
                .setAudience(Collections.singletonList(
                        "407772935914-jvr3iej6ik922cv0rip2mdl36b5bc0jo.apps.googleusercontent.com"))
                .build();

        try {
            System.out.println("try rabotaet");
            System.out.println("Verifying token with client_id: " + verifier.getAudience());
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                System.out.println("Valid ID token: " + idToken.getPayload());
                return idToken.getPayload();
            } else {
                System.out.println("Invalid ID token received from client.");
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("catch rabotaet");
            System.err.println("Error during token verification: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JwtResponse generateJwtResponse(User user) {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        return jwtResponse;
    }

    @Override
    public User UserFirstLogin(GoogleIdToken.Payload payload) {
        if(userRepository.findByUsername(payload.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();
        user.setUsername(payload.getEmail());
        user.setPassword(null);
        user.setPasswordConfirmation(null);

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
    public JwtResponse loginWithGoogle(GoogleAuthUser googleAuthUser) {
        if (googleAuthUser.getToken() == null ) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = verifyToken(googleAuthUser.getToken());

        if (payload == null) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        // Если пользователь уже существует
        if (userRepository.findByUsername(payload.getEmail()).isPresent()) {
            User user = userService.getByUsername(payload.getEmail());
            return generateJwtResponse(user);
        }

        // Если первый вход
        else {
            User user = UserFirstLogin(payload);
            return generateJwtResponse(user);
        }
    }
}
