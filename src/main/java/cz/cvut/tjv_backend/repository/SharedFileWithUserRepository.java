package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.SharedFileWithUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharedFileWithUserRepository extends JpaRepository<SharedFileWithUser, UUID> {

    Optional<SharedFileWithUser> findByFileIdAndSharedWithId(UUID fileId, UUID user);

    List<SharedFileWithUser> findAllBySharedWithId(UUID userId);
    // New Method: Find all shared files where the file's owner ID matches
    @Query("SELECT s FROM SharedFileWithUser s WHERE s.file.owner.id = :ownerId")
    List<SharedFileWithUser> findAllByFileOwnerId(UUID ownerId);

    List<SharedFileWithUser> findAllByFileId(UUID fileId);
}

