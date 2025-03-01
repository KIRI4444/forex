package com.example.service;


import com.example.web.dto.auth.JwtRequest;
import com.example.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
