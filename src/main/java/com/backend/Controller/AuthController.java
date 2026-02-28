package com.backend.Controller;

import com.backend.Dto.Request.LoginRequest;
import com.backend.Dto.Response.ApiResponse;
import com.backend.Dto.Response.LoginResponse;
import com.backend.Dto.Response.UserResponse;
import com.backend.Service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
@RequiredArgsConstructor
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

}
