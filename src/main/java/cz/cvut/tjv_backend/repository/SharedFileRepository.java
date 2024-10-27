package cz.cvut.tjv_backend.repository;


import cz.cvut.tjv_backend.entity.SharedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SharedFileRepository extends JpaRepository<SharedFile, UUID> {
    List<SharedFile> findFilesByGroupId(UUID groupId);

    boolean existsByFileIdAndSharedWithId(UUID fileId, UUID targetUserId);

    List<SharedFile> findBySharedWithId(UUID userId);

    @Query(value = "SELECT sf.* FROM shared_files sf " +
            "JOIN user_groups ug ON ug.group_id = sf.group_id " +
            "WHERE ug.user_id = :userId", nativeQuery = true)
    List<SharedFile> findFilesSharedWithGroupsByUser(@Param("userId") UUID userId);

}

