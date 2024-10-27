package cz.cvut.tjv_backend.repository;

import cz.cvut.tjv_backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    List<Group> findByUsersId(UUID userId);
}

