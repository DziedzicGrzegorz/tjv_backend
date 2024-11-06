package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import cz.cvut.tjv_backend.entity.utils.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, UUID> {
    Set<UserGroupRole> findByGroup(Group group);

    boolean existsByUserAndGroup(User user, Group group);

    boolean existsByUserAndGroupAndRole(User user, Group group, Role role);

    List<UserGroupRole> findByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserGroupRole ugr WHERE ugr.user.id = :userId AND ugr.group.id = :groupId")
    void deleteByUserIdAndGroupId(@Param("userId") UUID userId, @Param("groupId") UUID groupId);
}
