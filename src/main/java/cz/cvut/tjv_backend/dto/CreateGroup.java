package cz.cvut.tjv_backend.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CreateGroup {
    private String name;
    private String description;
    private Set<String> usersToAdd;
    private UUID founderId;

}
