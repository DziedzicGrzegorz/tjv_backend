package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.SharedFileWithUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharedFileWithUserRepository extends JpaRepository<SharedFileWithUser, UUID> {

    Optional<SharedFileWithUser> findByFileIdAndSharedWithId(UUID fileId, UUID user);

    List<SharedFileWithUser> findAllBySharedWithId(UUID userId);
}

