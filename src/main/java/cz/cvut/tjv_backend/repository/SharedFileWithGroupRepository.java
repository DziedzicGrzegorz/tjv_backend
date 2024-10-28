package cz.cvut.tjv_backend.repository;
import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SharedFileWithGroupRepository extends JpaRepository<SharedFileWithGroup, UUID> {

    // Find all files shared with a specific group
    @Query("SELECT sfwg FROM SharedFileWithGroup sfwg WHERE sfwg.group.id = :groupId")
    List<SharedFileWithGroup> findFilesSharedWithGroup(@Param("groupId") UUID groupId);

    // Find all groups a specific file is shared with
    @Query("SELECT sfwg.group FROM SharedFileWithGroup sfwg WHERE sfwg.file.id = :fileId")
    List<Group> findGroupsFileIsSharedWith(@Param("fileId") UUID fileId);

    // Find shared file details by file ID and group ID
    @Query("SELECT sfwg FROM SharedFileWithGroup sfwg WHERE sfwg.file.id = :fileId AND sfwg.group.id = :groupId")
    Optional<SharedFileWithGroup> findSharedFileDetails(@Param("fileId") UUID fileId, @Param("groupId") UUID groupId);
}

