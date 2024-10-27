package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
    @Query("SELECT f FROM File f WHERE f.owner.id = :userId OR f.id IN (SELECT sf.file.id FROM SharedFile sf WHERE sf.sharedWith.id = :userId)")
    List<File> findAllFilesByUserId(@Param("userId") UUID userId);

    @Query("SELECT f FROM File f WHERE f.owner.id = :userId")
    List<File> findFilesOwnedByUser(@Param("userId") UUID userId);

}
