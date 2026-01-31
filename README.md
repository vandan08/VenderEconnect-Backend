# VenderConnect Backend

**"Everything You Need, All in One Place"**

VenderConnect is a comprehensive service marketplace backend that connects users with vendors for various services. Built with Spring Boot and MongoDB, it provides secure authentication, job management, and profile handling.

## 🚀 Features

- **Multi-Role Authentication**: Users, Vendors, and Admins with JWT security
- **Google OAuth Integration**: Seamless login with Google accounts
- **Job Management**: Create, manage, and track service requests
- **Profile Management**: User profiles with image upload support
- **Multi-Category Services**: Vendors can offer multiple service categories
- **Budget Range System**: Dynamic budget ranges for jobs
- **File Upload**: Secure profile image handling
- **Admin Dashboard**: Complete user and vendor management
- **Persistent Data**: MongoDB with persistent storage

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.x
- **Database**: MySQL with Hibernate ORM
- **Security**: Spring Security + JWT
- **Authentication**: Google OAuth 2.0
- **File Storage**: Local file system
- **Build Tool**: Maven
- **Java Version**: 17+

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 or higher
- Google Cloud Console account (for OAuth)

## ⚙️ Configuration

### 1. Google OAuth Setup
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create OAuth 2.0 credentials
3. Add authorized origins: `http://localhost:5173`
4. Update `application.yml`:

```yaml
google:
  oauth:
    client-id: 968790597541-a4ukmkd56rjan720jqnhn8r32tgup6ru.apps.googleusercontent.com
    client-secret: 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
```

### 2. Database Setup
```sql
CREATE DATABASE vendor_connect;
```

### 3. Application Configuration
```yaml
server:
  port: 8083

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vendor_connect
    username: root
    password: your_mysql_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 5MB

jwt:
  secret: OneMe-SecretKey-2024
  expiration: 86400000
```

## 🚀 Getting Started

### 1. Clone and Setup
```bash
git clone <repository-url>
cd VenderConnect-Backend
```

### 2. Database Setup
```bash
# Install MySQL and create database
mysql -u root -p
CREATE DATABASE vendor_connect;
EXIT;
```

### 3. Configure Application
- Update MySQL credentials in `application.yml`
- Set your Google Client ID and Secret

### 4. Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8083`

## 📚 API Endpoints

### Authentication
- `POST /api/auth/register/user` - User registration
- `POST /api/auth/register/vendor` - Vendor registration
- `POST /api/auth/login/user` - User login
- `POST /api/auth/login/vendor` - Vendor login
- `POST /api/auth/login/admin` - Admin login
- `POST /api/auth/google` - Google OAuth authentication

### Job Management
- `GET /api/jobs/user` - Get user's jobs
- `POST /api/jobs` - Create new job
- `GET /api/jobs/vendor` - Get vendor's jobs
- `GET /api/jobs/available` - Get available jobs for vendors
- `POST /api/jobs/{jobId}/accept` - Accept job (vendor)
- `PUT /api/jobs/{jobId}/status` - Update job status

### Profile Management
- `GET /api/profile/user` - Get user profile
- `GET /api/profile/vendor` - Get vendor profile
- `PUT /api/profile/user` - Update user profile
- `PUT /api/profile/vendor` - Update vendor profile
- `POST /api/profile/change-password` - Change password

### File Upload
- `POST /api/files/upload-profile-image` - Upload profile image

### Admin
- `GET /api/admin/users` - Get all users
- `GET /api/admin/vendors` - Get all vendors
- `DELETE /api/admin/users/{userId}` - Delete user
- `DELETE /api/admin/vendors/{vendorId}` - Delete vendor

## 🗄️ Database Schema

### MySQL Tables

#### users
- `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- `name` (VARCHAR, NOT NULL)
- `email` (VARCHAR, UNIQUE, NOT NULL)
- `password` (VARCHAR, NOT NULL)
- `location` (VARCHAR)
- `profile_image` (VARCHAR)

#### vendors
- `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- `name` (VARCHAR, NOT NULL)
- `email` (VARCHAR, UNIQUE, NOT NULL)
- `password` (VARCHAR, NOT NULL)
- `location` (VARCHAR)
- `profile_image` (VARCHAR)
- `is_available` (BOOLEAN, DEFAULT TRUE)

#### jobs
- `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- `job_title` (VARCHAR, NOT NULL)
- `description` (TEXT)
- `service_category` (VARCHAR, NOT NULL)
- `location` (VARCHAR)
- `budget_min` (DOUBLE)
- `budget_max` (DOUBLE)
- `status` (VARCHAR, DEFAULT 'pending')
- `user_id` (VARCHAR)
- `assigned_vendor` (VARCHAR)
- `created_at` (DATETIME)
- `updated_at` (DATETIME)

#### admins
- `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- `name` (VARCHAR, NOT NULL)
- `email` (VARCHAR, UNIQUE, NOT NULL)
- `password` (VARCHAR, NOT NULL)

#### Collection Tables
- `user_jobs` - User's posted jobs
- `vendor_service_categories` - Vendor's service categories
- `vendor_team_members` - Vendor's team members
- `vendor_jobs` - Vendor's accepted jobs

### Default Admin
- Email: `admin@vendrconnect.com`
- Password: `admin123`

## 🔒 Security Features

- JWT-based authentication
- BCrypt password encryption
- Google OAuth integration
- Role-based access control
- File upload validation
- CORS configuration
- JPA/Hibernate ORM security
- MySQL prepared statements

## 📁 Project Structure

```
src/main/java/com/vendrconnect/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data transfer objects
├── model/          # Entity models
├── repository/     # Data repositories
├── security/       # Security configuration
├── service/        # Business logic
└── util/           # Utility classes

# MySQL database connection
# Tables auto-created by Hibernate DDL

uploads/
└── profile-images/ # User profile images
```

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## 📦 Building for Production

```bash
# Create production build
mvn clean package -Pprod

# Run production jar
java -jar target/vendrconnect-backend-1.0.0.jar
```

## 🔧 Troubleshooting

### Common Issues

1. **Port Conflicts**: Backend runs on port 8083
2. **MySQL Connection**: Ensure MySQL is running and credentials are correct
3. **Database Creation**: Make sure `vendor_connect` database exists
4. **Google OAuth**: Ensure correct client ID and origins
5. **File Uploads**: Check directory permissions for `uploads/`
6. **Hibernate DDL**: Tables are auto-created on first run

### Logs
```bash
# View application logs
tail -f logs/application.log

# Debug mode
mvn spring-boot:run -Dspring-boot.run.arguments=--logging.level.com.vendrconnect=DEBUG
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team

---

**VenderConnect Backend** - Powering the unified service marketplace platform.