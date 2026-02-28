package com.backend.Service;

import com.backend.Dto.Request.UserRegisterRequest;
import com.backend.Dto.Response.PaginationResponse;
import com.backend.Dto.Response.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRegisterRequest request);

    UserResponse getUserById(String userId);

    PaginationResponse<UserResponse> getAllUsers(int page, int size, String search, String sortBy, String sortDir);

}
