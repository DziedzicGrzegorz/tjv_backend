package cz.cvut.tjv_backend.exception;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String path;
    @Nullable
    private Map<String, String> details;
}

