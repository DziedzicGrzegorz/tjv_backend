package cz.cvut.tjv_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "files")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String blobUrl;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SharedFileWithUser> sharedWithUsers;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SharedFileWithGroup> sharedWithGroups;
}
