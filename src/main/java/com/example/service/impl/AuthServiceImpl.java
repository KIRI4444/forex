package com.example.service.impl;

import com.example.domain.GoogleAuth.GoogleAuthUser;
import com.example.domain.user.Role;
import com.example.domain.user.User;
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

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка аутентификации", e);
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
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.gson.GsonFactory()
        )
                .setAudience(Collections.singletonList(
                        "259962957478-f0vp622okbc19vrmqm96cdatgmhj104f.apps.googleusercontent.com"))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
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
        User user = new User();
        user.setUsername(payload.getEmail());
        user.setPassword(null);
        user.setPasswordConfirmation(null);
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    @Override
    public JwtResponse loginWithGoogle(GoogleAuthUser googleAuthUser) {
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
