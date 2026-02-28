//package com.backend.initializer;
//
//import com.backend.Entity.Role;
//import com.backend.Enum.UserRole;
//import com.backend.Repository.RoleRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final RoleRepository roleRepository;
//
//    @PostConstruct
//    public void init() {
//
//        if (roleRepository.count() == 0) {
//            roleRepository.save(new Role(null, UserRole.ADMIN));
//            roleRepository.save(new Role(null, UserRole.USER));
//            roleRepository.save(new Role(null, UserRole.GUEST));
//            roleRepository.save(new Role(null, UserRole.STAFF));
//        }
//    }
//}