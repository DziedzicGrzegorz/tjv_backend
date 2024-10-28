package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import cz.cvut.tjv_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    // Find group by name (assuming name is unique)
    Optional<Group> findByName(String name);

    // Find group by ID
    Optional<Group> findById(UUID id);

    // Find groups by admin user ID
// Find groups by admin user ID
    @Query("SELECT ugr.group FROM UserGroupRole ugr WHERE ugr.user.id = :adminId AND ugr.role = 'ADMIN'")
    List<Group> findGroupsByAdminId(@Param("adminId") UUID adminId);

    // Find groups by user ID (not admin, just member)
    @Query("SELECT g FROM Group g JOIN g.userRoles ugr WHERE ugr.user.id = :userId")
    List<Group> findGroupsByUserId(@Param("userId") UUID userId);

    // Find all users in a group by group ID
    @Query("SELECT ugr.user FROM UserGroupRole ugr WHERE ugr.group.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") UUID groupId);

    // Find all admin users in a group by group ID
    @Query("SELECT ugr.user FROM UserGroupRole ugr WHERE ugr.group.id = :groupId AND ugr.role = 'ADMIN'")
    List<User> findAdminsByGroupId(@Param("groupId") UUID groupId);

    // Find group by founder ID
    @Query("SELECT ugr.group FROM UserGroupRole ugr WHERE ugr.user.id = :founderId AND ugr.role = 'FOUNDER'")
    List<Group> findGroupsByFounderId(@Param("founderId") UUID founderId);

    // Find all files shared with a specific group
    @Query("SELECT sfwg FROM SharedFileWithGroup sfwg WHERE sfwg.group.id = :groupId")
    List<SharedFileWithGroup> findFilesByGroupId(@Param("groupId") UUID groupId);
}
