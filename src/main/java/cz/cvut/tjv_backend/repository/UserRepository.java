package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find a User by email
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    // Check if a User exists by email
    boolean existsByEmail(String email);

    boolean existsByUsername(String email);

    // Update a User's username by IDw
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsernameById(@Param("id") UUID id, @Param("username") String username);

    // Update a User's email by ID
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    void updateEmailById(@Param("id") UUID id, @Param("email") String email);

    // Update a User's password by ID
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :passwordHash WHERE u.id = :id")
    void updatePasswordById(@Param("id") UUID id, @Param("passwordHash") String passwordHash);

    // Delete a User by email
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.email = :email")
    void deleteByEmail(@Param("email") String email);
}
