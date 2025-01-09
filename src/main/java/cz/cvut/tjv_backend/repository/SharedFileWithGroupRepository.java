package cz.cvut.tjv_backend.repository;

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

    Optional<SharedFileWithGroup> findByFileIdAndGroupId(UUID fileId, UUID groupId);

    boolean existsByFileIdAndGroupId(UUID fileId, UUID groupId);
    @Query("SELECT s FROM SharedFileWithGroup s WHERE s.file.owner.id = :ownerId")
    List<SharedFileWithGroup> findAllByFileOwnerId(UUID ownerId);

   List<SharedFileWithGroup> findAllByFileId(UUID fileId);
}

