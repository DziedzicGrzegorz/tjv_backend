# File Sharing System Backend

A Spring Boot backend application for a secure file sharing system with user and group-based access control.

## Overview

This application serves as a backend for a file sharing platform that allows users to:

- Upload and manage files
- Share files with individual users
- Create and manage groups
- Share files with groups
- Control access permissions

## Entity Structure

### Core Entities

- **User**: Manages authentication and user details
- **File**: Represents uploaded files with metadata
- **Group**: Represents user groups for sharing
- **UserGroupRole**: Manages user roles within groups
- **SharedFileWithUser**: Tracks file sharing with individual users
- **SharedFileWithGroup**: Tracks file sharing with groups

### Entity Relationships

- Users can upload files (owner relationship)
- Files can be shared with individual users with specific permissions
- Users can form groups with different roles (admin, member, etc.)
- Files can be shared with groups with specific permissions

## Technical Details

### Technology Stack

- **Framework**: Spring Boot
- **Database**: JPA/Hibernate with PostgreSQL
- **Security**: Spring Security
- **Build Tool**: Maven/Gradle
- **Documentation**:Swagger

### Key Features

- **Secure Authentication**: JWT-based authentication
- **Role-based Access Control**: Different permission levels
- **Version Control**: File versioning support
- **Efficient Storage**: Azure Blob Storage integration
- **API Documentation**: Swagger/OpenAPI integration
- **Large File Support**: Up to 50MB file uploads

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven or Gradle
- PostgreSQL database
- Azure Storage account (for file blob storage)
- Environment with necessary configuration parameters

### Installation

1. Clone the repository:
```
git clone https://gitlab.fit.cvut.cz/dziedgrz/tjv_backend.git
cd tjv_backend
```

2. Configure the application by filling in the necessary values in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

# Azure Storage Configuration (for file blob storage)
azure.storage.connection.string=your_azure_connection_string
azure.storage.container.name=your_container_name
azure.storage.queue.name=your_queue_name

# JWT Security Configuration
application.security.jwt.secret-key=your_jwt_secret_key
application.security.jwt.access_expiration=3600000
application.security.jwt.refresh_expiration=86400000
```

3. Build the project:
```
mvn clean install
```

4. Run the application:
```
mvn spring-boot:run
```

### API Documentation

Once the application is running, access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Development

### Project Structure

```
src/main/java/cz/cvut/tjv_backend/
├── TjvBackendApplication.java       # Main application entry point
├── authConfig/                      # Authentication and security configuration
│   ├── CustomAuthenticationEntryPoint.java
│   ├── JwtFilter.java              
│   ├── SecurityConfig.java          # Spring Security configuration
│   ├── SecurityConstants.java       # Security constants and public endpoints
│   └── TOKEN_TYPE.java              # Token type enum (ACCESS, REFRESH)
├── config/                          # Application configuration
│   ├── BeansConfig.java             # Bean definitions
│   └── OpenAPIConfig.java           # Swagger/OpenAPI configuration
├── controller/                      # REST API controllers
│   ├── AuthenticationController.java # Authentication endpoints
│   ├── FileController.java          # File management endpoints
│   ├── GroupController.java         # Group management endpoints
│   ├── SharedFileController.java    # File sharing endpoints  
│   └── UserController.java          # User management endpoints
├── dto/                             # Data Transfer Objects
│   ├── file/                        # File-related DTOs
│   ├── group/                       # Group-related DTOs
│   ├── user/                        # User-related DTOs
│   ├── userGroup/                   # User-group relationship DTOs
│   └── ...                          # Other DTOs for data transfer
├── entity/                          # JPA entities (database models)
│   ├── File.java                    # File entity
│   ├── Group.java                   # Group entity 
│   ├── SharedFileWithGroup.java     # File sharing with group relationship
│   ├── SharedFileWithUser.java      # File sharing with user relationship
│   ├── User.java                    # User entity
│   ├── UserGroupRole.java           # User-group role relationship
│   └── utils/                       # Entity utility classes and enums
├── exception/                       # Exception handling
│   ├── ErrorResponse.java           # Standardized error response model
│   ├── Exceptions.java              # Custom exception definitions
│   └── GlobalExceptionHandler.java  # Global exception handler
├── mapper/                          # Object mappers
│   ├── FileMapper.java              # File entity/DTO mapping
│   ├── GroupMapper.java             # Group entity/DTO mapping
│   ├── SharedFileWithGroupMapper.java
│   ├── SharedFileWithUserMapper.java
│   └── UserMapper.java              # User entity/DTO mapping
├── repository/                      # Spring Data JPA repositories
│   ├── FileRepository.java          # File data access
│   ├── GroupRepository.java         # Group data access
│   ├── SharedFileWithGroupRepository.java
│   ├── SharedFileWithUserRepository.java
│   ├── UserGroupRoleRepository.java 
│   └── UserRepository.java          # User data access
├── request/                         # Request models
│   ├── auth/                        # Authentication requests
│   ├── ChangePasswordRequest.java
│   ├── CreateGroupRequest.java
│   ├── FileSharingWithGroupRequest.java
│   ├── FileSharingWithUserRequest.java
│   └── ...                          # Other request models
├── service/                         # Business logic services
│   ├── AuthenticationService.java   # Authentication functionality
│   ├── FileService.java             # File management functionality
│   ├── GroupService.java            # Group management functionality
│   ├── JwtService.java              # JWT handling
│   ├── SharedFileService.java       # File sharing functionality
│   └── UserService.java             # User management functionality
└── storage/                         # External storage integration
    ├── AzureBlobStorageService.java # Azure Blob Storage implementation
    ├── azure/                       # Azure configuration
    └── interfaces/                  # Storage service interfaces
```

### Database Schema

The application uses the following database tables:
- `users`
- `files`
- `groups`
- `user_group_roles`
- `shared_files_with_user`
- `shared_files`

## Testing

Run the tests with:
```
mvn test
```

## Deployment

The application can be deployed as a JAR file or Docker container.

### Docker

```
docker build -t tjv-backend .
docker run -p 8080:8080 tjv-backend
```

## Contributing

1. Create a feature branch
2. Commit your changes
3. Push your branch and submit a merge request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Faculty of Information Technology, Czech Technical University in Prague