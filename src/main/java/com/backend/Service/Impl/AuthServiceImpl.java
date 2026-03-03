package com.backend.Service.Impl;

import com.backend.Dto.Request.LoginRequest;
import com.backend.Dto.Response.LoginResponse;
import com.backend.Dto.Response.UserResponse;
import com.backend.Entity.User;
import com.backend.Exception.ResourceNotFoundException;
import com.backend.Repository.UserRepository;
import com.backend.Service.AuthService;
import com.backend.Service.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest request) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            String accessToken = jwtService.generateAccessToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();

        } catch (Exception ex) {
            log.error("Login failed: {}", ex.getMessage());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public UserResponse getLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User Not Found With ", "email", authentication.getName()
                ));

        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().getName().name() : null)
                .build();
    }

    @Override
    public LoginResponse loginWithGoogle(String idToken) {

        try {

            GoogleIdToken.Payload payload = verifyToken(idToken);

            String email = payload.getEmail();
            String fullName = (String) payload.get("name");

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setFullName(fullName);
                        newUser.setPassword(UUID.randomUUID().toString());
                        return userRepository.save(newUser);
                    });

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            null
                    );

            String accessToken = jwtService.generateAccessToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();

        } catch (Exception e) {
            log.error("Google login failed: {}", e.getMessage());
            throw new BadCredentialsException("Google authentication failed");
        }
    }

    public GoogleIdToken.Payload verifyToken(String idToken) {

        try {

            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            new NetHttpTransport(),
                            new GsonFactory()
                    )
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            GoogleIdToken token = verifier.verify(idToken);

            if (token == null) {
                throw new BadCredentialsException("Invalid Google Token");
            }

            return token.getPayload();

        } catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage());
            throw new BadCredentialsException("Google authentication failed");
        }
    }
}