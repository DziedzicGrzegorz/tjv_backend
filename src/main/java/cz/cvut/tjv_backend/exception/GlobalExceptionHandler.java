package cz.cvut.tjv_backend.exception;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(Exceptions.InvalidTokenSignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenSignatureException(Exceptions.InvalidTokenSignatureException ex, WebRequest request) {
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
}
