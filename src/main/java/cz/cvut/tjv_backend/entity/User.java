package cz.cvut.tjv_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "sharedWith", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SharedFileWithUser> sharedFiles;

    @Transient
    public Set<Group> getGroups() {
        Set<Group> groups = new HashSet<>();
        for (UserGroupRole role : groupRoles) {
            groups.add(role.getGroup());
        }
        return groups;
    }
}

