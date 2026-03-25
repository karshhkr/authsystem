package com.example.authsystem.repository;

import com.example.authsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    //  non-deleted
    Optional<User> findByEmailAndDeletedFalse(String email);



    @Query("""
        SELECT u FROM User u
        WHERE (:deleted IS NULL OR u.deleted = :deleted)
          AND (:role IS NULL OR u.role = :role)
          AND (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))
                         OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<User> searchUsers(
            @Param("search") String search,
            @Param("role") String role,
            @Param("deleted") Boolean deleted,
            Pageable pageable
    );
}
