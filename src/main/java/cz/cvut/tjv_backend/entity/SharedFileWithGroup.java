package cz.cvut.tjv_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shared_files")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class SharedFileWithGroup {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    @Setter
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @Setter
    private Group group;

    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    private LocalDateTime sharedAt;

}
