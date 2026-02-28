package com.backend.Service.Impl;

import com.backend.Dto.Request.UserRegisterRequest;
import com.backend.Dto.Response.PaginationResponse;
import com.backend.Dto.Response.UserResponse;
import com.backend.Entity.Role;
import com.backend.Entity.User;
import com.backend.Enum.UserRole;
import com.backend.Repository.RoleRepository;
import com.backend.Repository.UserRepository;
import com.backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    log.error("User already exists : {}", request.getEmail());
                    throw new RuntimeException("User already exists");
                });
        Role role = roleRepository.findByName(UserRole.USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .phone(request.getPhone())
                .build();
        log.info("User registered successfully from UserServiceImpl  : {}", user);
        userRepository.save(user);

        return UserResponse.convertToUserResponse(user);
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.convertToUserResponse(user);
    }

    @Override
    public PaginationResponse<UserResponse> getAllUsers(int page, int size, String search, String sortBy, String sortDir) {
        String sortField = Optional.ofNullable(sortBy).filter(s -> !s.isBlank()).orElse("createdAt");
        Sort.Direction sortDirection = Optional.ofNullable(sortDir)
                .map(Sort.Direction::fromString)
                .orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(sortDirection, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        String searchQuery = Optional.ofNullable(search).filter(s -> !s.isBlank()).orElse("");

        var userList = searchQuery.isEmpty()
                ? userRepository.findAll(pageable)
                : userRepository.searchUsers(searchQuery, pageable);

        List<UserResponse> userResponses = userList.stream().map(UserResponse::convertToUserResponse)
                .toList();

        return PaginationResponse.<UserResponse>builder()
                .data(userResponses)
                .page(userList.getNumber())
                .size(userList.getSize())
                .totalElements(userList.getTotalElements())
                .totalPages(userList.getTotalPages())
                .isLastPage(userList.isLast())
                .sortBy(sortField)
                .sortDir(sortDirection.name())
                .build();
    }
}
