package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import cz.cvut.tjv_backend.entity.SharedFileWithUser;
import cz.cvut.tjv_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharedFileWithUserRepository extends JpaRepository<SharedFileWithUser, UUID> {

    // Find all files shared with a specific user
    @Query("SELECT sfwu FROM SharedFileWithUser sfwu WHERE sfwu.sharedWith.id = :userId")
    List<SharedFileWithUser> findFilesSharedWithUser(@Param("userId") UUID userId);

    // Find all users a specific file is shared with
    @Query("SELECT sfwu.sharedWith FROM SharedFileWithUser sfwu WHERE sfwu.file.id = :fileId")
    List<User> findUsersFileIsSharedWith(@Param("fileId") UUID fileId);

    // Find shared file details by file ID and user ID
    @Query("SELECT sfwu FROM SharedFileWithUser sfwu WHERE sfwu.file.id = :fileId AND sfwu.sharedWith.id = :userId")
    Optional<SharedFileWithUser> findSharedFileDetails(@Param("fileId") UUID fileId, @Param("userId") UUID userId);

    // Find all files shared by a specific user with information about who it was shared with
    @Query("SELECT sfwg FROM SharedFileWithGroup sfwg")
    List<SharedFileWithGroup> findAllFilesSharedWithGroupsWithDetails();


    Optional<SharedFileWithUser> findByFileIdAndSharedWithId(UUID fileId, UUID user);
}

