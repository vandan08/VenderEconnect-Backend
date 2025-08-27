# OneMe Backend

**"Everything You Need, All in One Place"**

OneMe is a comprehensive service marketplace backend that connects users with vendors for various services. Built with Spring Boot and MongoDB, it provides secure authentication, job management, and profile handling.

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
- **Database**: MongoDB (Embedded with persistent storage)
- **Security**: Spring Security + JWT
- **Authentication**: Google OAuth 2.0
- **File Storage**: Local file system
- **Build Tool**: Maven
- **Java Version**: 17+

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
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

### 2. Application Configuration
```yaml
server:
  port: 8083

spring:
  data:
    mongodb:
      database: oneme
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
cd VenderEconnect-Backend
```

### 2. Configure Google OAuth
- Replace placeholder values in `application.yml`
- Set your Google Client ID and Secret

### 3. Run the Application
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

### Users
- Personal information and authentication
- Job posting history
- Profile image support

### Vendors
- Business information
- Multiple service categories
- Team member management
- Availability status

### Jobs
- Service requests with budget ranges
- Status tracking
- Category-based filtering

### Admins
- System administration accounts
- Default admin: `admin@oneme.com` / `admin123`

## 🔒 Security Features

- JWT-based authentication
- BCrypt password encryption
- Google OAuth integration
- Role-based access control
- File upload validation
- CORS configuration

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

data/
├── mongodb/        # Persistent database files
└── mongodb-binaries/ # MongoDB binaries cache

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
2. **MongoDB Issues**: Uses embedded MongoDB with persistent storage
3. **Google OAuth**: Ensure correct client ID and origins
4. **File Uploads**: Check directory permissions for `uploads/`

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

**OneMe Backend** - Powering the unified service marketplace platform.