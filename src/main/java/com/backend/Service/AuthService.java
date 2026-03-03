package com.backend.Service;

import com.backend.Dto.Request.LoginRequest;
import com.backend.Dto.Response.LoginResponse;
import com.backend.Dto.Response.UserResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserResponse getLoggedInUser();
    LoginResponse loginWithGoogle(String idToken);
}
