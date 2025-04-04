package com.example.web.utils;

import com.example.domain.exception.AccessDeniedException;
import com.example.web.security.JwtEntity;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Data
@Component
public class Util {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException();
        }
        return ((JwtEntity) authentication.getPrincipal()).getId();
    }
}
