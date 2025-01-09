package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //existsByName
    boolean existsByName(String name);

    //get all by user id
    @Query("SELECT g FROM Group g JOIN g.userRoles ugr WHERE ugr.user.id = :userId")
    List<Group> findAllByUserUserId(@Param("userId") UUID userId);


    // Paginated version
    @Query("SELECT g FROM Group g JOIN g.userRoles ugr WHERE ugr.user.id = :userId")
    Page<Group> findAllByUserUserId(@Param("userId") UUID userId, Pageable pageable);
}
