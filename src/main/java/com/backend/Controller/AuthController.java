package com.backend.Controller;

import com.backend.Dto.Request.GoogleLoginRequest;
import com.backend.Dto.Request.LoginRequest;
import com.backend.Dto.Response.ApiResponse;
import com.backend.Dto.Response.LoginResponse;
import com.backend.Dto.Response.UserResponse;
import com.backend.Service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request), "Login successful"));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getLoggedInUser() {
        return ResponseEntity.ok(ApiResponse.success(authService.getLoggedInUser(),
                "User profile retrieved successfully"));
    }

    @GetMapping("/oauth2/user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> googleUser(
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OAuth2User principal) {

        log.info("Google OAuth2 user logged in: {}", principal.getName());

        Map<String, Object> userInfo = Map.of(
                "name", Objects.requireNonNull(principal.getAttribute("name")),
                "email", Objects.requireNonNull(principal.getAttribute("email")),
                "picture", Objects.requireNonNull(principal.getAttribute("picture")),
                "emailVerified", Objects.requireNonNull(principal.getAttribute("email_verified"))
        );

        return ResponseEntity.ok(
                ApiResponse.success(userInfo, "Google user info retrieved successfully")
        );
    }
}
