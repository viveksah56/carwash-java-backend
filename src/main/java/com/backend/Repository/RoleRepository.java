package com.backend.Repository;

import com.backend.Entity.Role;
import com.backend.Enum.UserRole;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(UserRole name);
}
