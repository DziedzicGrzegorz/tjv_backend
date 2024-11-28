package cz.cvut.tjv_backend.exception;

import org.springframework.security.core.AuthenticationException;

public class Exceptions {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class StorageDeleteException extends RuntimeException {
        public StorageDeleteException(String message) {
            super(message);
        }
    }

    public static class StorageUploadException extends RuntimeException {
        public StorageUploadException(String message) {
            super(message);
        }
    }

    public static class StorageDownloadException extends RuntimeException {
        public StorageDownloadException(String message) {
            super(message);
        }
    }

    public static class InternalServerException extends RuntimeException {
        public InternalServerException(String message) {
            super(message);
        }
    }

    public static class ForbiddenOperationException extends RuntimeException {
        public ForbiddenOperationException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyMemberException extends RuntimeException {
        public UserAlreadyMemberException(String message) {
            super(message);
        }
    }

    public static class GroupOwnerCannotBeMemberException extends RuntimeException {
        public GroupOwnerCannotBeMemberException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class SelfFileShareException extends RuntimeException {
        public SelfFileShareException(String message) {
            super(message);
        }
    }

    public static class FileAlreadySharedException extends RuntimeException {
        public FileAlreadySharedException(String message) {
            super(message);
        }
    }

    public static class GroupAlreadyExistsException extends RuntimeException {
        public GroupAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class AccessTokenExpiredException extends AuthenticationException {
        public AccessTokenExpiredException(String msg, Throwable cause) {
            super(msg, cause);
        }

        public AccessTokenExpiredException(String msg) {
            super(msg);
        }
    }

    public static class InvalidTokenSignatureException extends RuntimeException {
        public InvalidTokenSignatureException(String message) {
            super(message);
        }

        public InvalidTokenSignatureException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    //token not found
    public static class MissingAccessToken extends RuntimeException {
        public MissingAccessToken(String message) {
            super(message);
        }

        public MissingAccessToken(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidRefreshTokenException extends RuntimeException {
        public InvalidRefreshTokenException(String message) {
            super(message);
        }
    }
}
