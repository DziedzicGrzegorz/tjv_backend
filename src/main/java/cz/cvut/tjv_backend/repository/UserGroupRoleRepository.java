package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, UUID> {

    boolean existsByUserAndGroup(User user, Group group);

    boolean existsByUserIdAndGroupId(UUID userId, UUID groupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserGroupRole ugr WHERE ugr.user.id = :userId AND ugr.group.id = :groupId")
    void deleteByUserIdAndGroupId(@Param("userId") UUID userId, @Param("groupId") UUID groupId);

    Optional<UserGroupRole> findByUserIdAndGroupId(UUID userId, UUID group);


    @Query("SELECT ugr.user FROM UserGroupRole ugr WHERE ugr.group.id = :groupId")
    Page<User> findUsersByGroupId(UUID groupId, Pageable pageable);


}
