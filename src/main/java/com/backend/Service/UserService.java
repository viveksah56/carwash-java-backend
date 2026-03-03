package com.backend.Service;

import com.backend.Dto.Request.UserRegisterRequest;
import com.backend.Dto.Response.PaginationResponse;
import com.backend.Dto.Response.UserResponse;

import java.util.Optional;

public interface UserService {

    UserResponse registerUser(UserRegisterRequest request);

    UserResponse getUserById(String userId);
    Optional<UserResponse> findByEmail(String email);

    UserResponse getUserByEmail(String email);

    PaginationResponse<UserResponse> getAllUsers(int page, int size, String search, String sortBy, String sortDir);

}
