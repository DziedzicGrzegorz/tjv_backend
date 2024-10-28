package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.entity.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, UUID> {
    Set<UserGroupRole> findByGroup(Group group);

    boolean existsByUserAndGroup(User user, Group group);

    boolean existsByUserAndGroupAndRole(User user, Group group, UserGroupRole.Role role);

    void deleteByUserAndGroup(User user, Group group);

    List<UserGroupRole> findByUserId(UUID userId);
}
