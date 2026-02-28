package com.backend.Entity;

import com.backend.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column( updatable = false, nullable = false, length = 36)
    private String roleId;
    @Column( unique = true, nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole name;
}
