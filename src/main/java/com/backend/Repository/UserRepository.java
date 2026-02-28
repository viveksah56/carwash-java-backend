package com.backend.Repository;

import com.backend.Entity.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT u
            FROM User u
            JOIN FETCH u.role
            WHERE u.email = :email
            """)
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
           SELECT u
           FROM User u
           WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
              OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
           """)
    Page<User> searchUsers(String searchQuery, Pageable pageable);
}
