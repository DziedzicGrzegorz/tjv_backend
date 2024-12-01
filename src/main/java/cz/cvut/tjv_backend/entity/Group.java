package cz.cvut.tjv_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    @Builder.Default
    private Set<UserGroupRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    @Builder.Default
    private Set<SharedFileWithGroup> sharedFiles = new HashSet<>();

}

