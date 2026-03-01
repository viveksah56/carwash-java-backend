package com.backend.Config;
import com.backend.Dto.Request.UserRegisterRequest;
import com.backend.Dto.Response.ApiResponse;
import com.backend.Dto.Response.LoginResponse;
import com.backend.Repository.RoleRepository;
import com.backend.Service.JwtService;
import com.backend.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper ;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("CustomOAuth2LoginSuccessHandler.onAuthenticationSuccess");
        log.info("Authentication: {}", authentication);

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");

        log.info("Google User - ID: {}, Name: {}, Email: {}", googleId, name, email);

        UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                .email(email)
                .fullName(name)
                .password(passwordEncoder.encode(UUID.randomUUID().toString() + googleId))
                .phone("1234567890")
                .build();

        log.info("Registering Google user: {}", registerRequest.getEmail());
        userService.registerUser(registerRequest);

        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        ApiResponse<LoginResponse> apiResponse = ApiResponse.success(loginResponse, "Google login successful");
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
