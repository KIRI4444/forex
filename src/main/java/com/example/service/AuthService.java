package com.example.service;


import com.example.domain.GoogleAuth.GoogleAuthUser;
import com.example.domain.user.User;
import com.example.web.dto.auth.JwtRequest;
import com.example.web.dto.auth.JwtResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

    GoogleIdToken.Payload verifyToken(String idTokenString);

    public JwtResponse loginWithGoogle(GoogleAuthUser googleAuthUser);

    public User UserFirstLogin(GoogleIdToken.Payload payload);

    public JwtResponse generateJwtResponse(User user);
}
