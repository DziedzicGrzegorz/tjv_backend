package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User u set u.passwordHash = :passwordHash where u.username like :username and u.email like :email")
    int updatePasswordHashByUsernameLikeAndEmailLike(@NonNull @Param("passwordHash")
                                                     String passwordHash, @NonNull @Param("username")
                                                     String username, @NonNull @Param("email") String email);

}
