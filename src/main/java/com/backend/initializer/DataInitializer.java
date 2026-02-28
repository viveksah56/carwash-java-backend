package com.backend.initializer;

import com.backend.Entity.Role;
import com.backend.Enum.UserRole;
import com.backend.Repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        for (UserRole roleEnum : UserRole.values()) {
            roleRepository.findByName(roleEnum).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleEnum);
                Role savedRole = roleRepository.save(role);
                log.info("Role initialized: {}", savedRole.getName());
                return savedRole;
            });
        }
    }
}