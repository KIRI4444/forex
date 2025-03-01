package com.example.web.security;

import com.example.domain.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }

        if (bearerToken != null) {
            try {
                if (jwtTokenProvider.validateToken(bearerToken)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException e) {
                sendErrorResponse(servletResponse, HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired");
                return;
            } catch (SignatureException e) {
                sendErrorResponse(servletResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature");
                return;
            } catch (JwtException e) {
                sendErrorResponse(servletResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            } catch (ResourceNotFoundException e) {
                sendErrorResponse(servletResponse, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void sendErrorResponse(ServletResponse servletResponse, int statusCode, String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setStatus(statusCode);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
