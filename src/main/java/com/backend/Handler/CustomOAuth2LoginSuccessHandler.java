package com.backend.Handler;

import com.backend.Dto.Request.UserRegisterRequest;
import com.backend.Repository.UserRepository;
import com.backend.Service.JwtService;
import com.backend.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
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
@NullMarked
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
//http://localhost:8080/api/v1/oauth2/authorization/google
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        assert oAuth2User != null;
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        log.info("OAuth2 login success for email {}", email);

        if (email != null && userRepository.findByEmail(email).isEmpty()) {

            UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                    .email(email)
                    .fullName(name)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString() + googleId))
                    .phone(null)
                    .build();

            userService.registerUser(registerRequest);
            log.info("New OAuth2 user registered {}", email);
        }

        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);

        log.info("JWT tokens generated for {}", email);

        response.sendRedirect("/auth/profile?token=" + accessToken);
    }
}