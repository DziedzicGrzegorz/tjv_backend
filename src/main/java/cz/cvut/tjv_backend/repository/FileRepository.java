package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    // findAll by return dto
    // Find files by owner ID
    @Query("SELECT f FROM File f WHERE f.owner.id = :ownerId")
    List<File> findFilesByOwnerId(@Param("ownerId") UUID ownerId);


    // Find all files that are not shared with any users or groups
    @Query("SELECT f FROM File f WHERE f.id NOT IN (SELECT sfwu.file.id FROM SharedFileWithUser sfwu) AND f.id NOT IN (SELECT sfwg.file.id FROM SharedFileWithGroup sfwg) AND f.owner.id = :ownerId")
    List<File> findFilesNotShared(@Param("ownerId") UUID ownerId);

    @Query("SELECT f FROM File f " +
            "LEFT JOIN SharedFileWithUser sfu ON sfu.file.id = f.id " +
            "LEFT JOIN SharedFileWithGroup sfg ON sfg.file.id = f.id " +
            "WHERE f.owner.id = :userId OR sfu.sharedWith.id = :userId OR sfg.group.id IN (SELECT ugr.group.id FROM UserGroupRole ugr WHERE ugr.user.id = :userId)")
    List<File> findAllFilesOwnedOrSharedWithUser(@Param("userId") UUID userId);
}