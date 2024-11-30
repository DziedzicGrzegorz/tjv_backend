package cz.cvut.tjv_backend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(Exceptions.ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenOperationException(Exceptions.ForbiddenOperationException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.StorageDeleteException.class)
    public ResponseEntity<ErrorResponse> handleStorageDeleteException(Exceptions.StorageDeleteException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.StorageUploadException.class)
    public ResponseEntity<ErrorResponse> handleStorageUploadException(Exceptions.StorageUploadException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.StorageDownloadException.class)
    public ResponseEntity<ErrorResponse> handleStorageDownloadException(Exceptions.StorageDownloadException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exceptions.InternalServerException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.UserAlreadyMemberException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyMemberException(Exceptions.UserAlreadyMemberException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.GroupOwnerCannotBeMemberException.class)
    public ResponseEntity<ErrorResponse> handleGroupOwnerCannotBeMemberException(Exceptions.GroupOwnerCannotBeMemberException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(Exceptions.UserAlreadyExistsException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.SelfFileShareException.class)
    public ResponseEntity<ErrorResponse> handleSelfFileShareException(Exceptions.SelfFileShareException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.FileAlreadySharedException.class)
    public ResponseEntity<ErrorResponse> handleFileAlreadySharedException(Exceptions.FileAlreadySharedException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.GroupAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleGroupAlreadyExistsException(Exceptions.GroupAlreadyExistsException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.AccessTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleAccessTokenExpiredException(Exceptions.AccessTokenExpiredException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.MissingAccessToken.class)
    public ResponseEntity<ErrorResponse> handleMissingAccessToken(Exceptions.MissingAccessToken ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(Exceptions.InvalidRefreshTokenException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.InvalidAccessTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAccessTokenException(Exceptions.InvalidAccessTokenException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(Exceptions.InvalidTokenSignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenSignatureException(Exceptions.InvalidTokenSignatureException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        String message = "Validation failed for one or more fields";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .details(validationErrors)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Invalid argument type")
                .path(request.getDescription(false).replace("uri=", ""))
                .details(Map.of("parameter", ex.getName(), "invalidValue", String.valueOf(ex.getValue())))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
