package cz.cvut.tjv_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getReason() != null ? ex.getReason() : "Unexpected error", request);
    }

    @ExceptionHandler(Exceptions.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exceptions.NotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exceptions.BadRequestException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exceptions.UnauthorizedException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }
    @ExceptionHandler(Exceptions.StorageDeleteException.class)
    public ResponseEntity<ErrorResponse> handleStorageDeleteException(Exceptions.StorageDeleteException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting file from storage", request);
    }
    @ExceptionHandler(Exceptions.StorageUploadException.class)
    public ResponseEntity<ErrorResponse> handleStorageUploadException(Exceptions.StorageUploadException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading file to storage", request);
    }
    @ExceptionHandler(Exceptions.StorageDownloadException.class)
    public ResponseEntity<ErrorResponse> handleStorageDownloadException(Exceptions.StorageDownloadException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading file from storage", request);
    }
    @ExceptionHandler(Exceptions.InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exceptions.InternalServerException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }
}
