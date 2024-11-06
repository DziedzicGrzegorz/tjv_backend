package cz.cvut.tjv_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shared_files_with_user")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class SharedFileWithUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    @Setter
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_user_id", nullable = false)
    @Setter
    private User sharedWith;

    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    private LocalDateTime sharedAt;

}

