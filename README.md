# VendrConnect Backend

A Spring Boot backend service for the VendrConnect marketplace application.

## Technologies Used

- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data MongoDB
- JWT Authentication
- Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+

## Setup Instructions

### 1. Install MongoDB

Download and install MongoDB from [https://www.mongodb.com/try/download/community](https://www.mongodb.com/try/download/community)

Start MongoDB service:
```bash
# Windows
net start MongoDB

# macOS/Linux
sudo systemctl start mongod
```

### 2. Clone and Setup

```bash
cd backend
mvn clean install
```

### 3. Configuration

The application uses the following default configuration in `application.yml`:

- **Server Port**: 8080
- **MongoDB URI**: mongodb://localhost:27017/vendrconnect
- **JWT Secret**: VendrConnectSecretKey2024!@#$%^&*()
- **JWT Expiration**: 24 hours

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

- `POST /api/auth/register/user` - Register a new user
- `POST /api/auth/register/vendor` - Register a new vendor
- `POST /api/auth/login/user` - User login
- `POST /api/auth/login/vendor` - Vendor login

### Jobs (Protected Routes)

- `POST /api/jobs` - Create a new job (User only)
- `GET /api/jobs/user` - Get user's posted jobs
- `GET /api/jobs/vendor` - Get vendor's assigned jobs
- `GET /api/jobs/available` - Get available jobs for vendor
- `POST /api/jobs/{jobId}/accept` - Accept a job (Vendor only)
- `PUT /api/jobs/{jobId}/status` - Update job status
- `GET /api/jobs/{jobId}` - Get job details

### Vendor Management (Protected Routes)

- `GET /api/vendor/profile` - Get vendor profile
- `PUT /api/vendor/availability` - Update availability status
- `POST /api/vendor/team` - Add team member
- `DELETE /api/vendor/team/{memberName}` - Remove team member
- `PUT /api/vendor/team/{memberName}` - Update team member

## Request/Response Examples

### User Registration
```json
POST /api/auth/register/user
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "location": "New York"
}
```

### Vendor Registration
```json
POST /api/auth/register/vendor
{
  "name": "ABC Plumbing",
  "email": "abc@plumbing.com",
  "password": "password123",
  "serviceCategory": "plumber",
  "location": "New York"
}
```

### Create Job
```json
POST /api/jobs
Authorization: Bearer <token>
{
  "jobTitle": "Fix Kitchen Sink",
  "description": "Kitchen sink is leaking",
  "location": "New York",
  "serviceCategory": "plumber"
}
```

### Update Availability
```json
PUT /api/vendor/availability
Authorization: Bearer <token>
{
  "status": "online"
}
```

## Database Schema

### Users Collection
- `_id`: ObjectId
- `name`: String
- `email`: String (unique)
- `password`: String (hashed)
- `location`: String
- `jobsPosted`: Array of job IDs

### Vendors Collection
- `_id`: ObjectId
- `name`: String
- `email`: String (unique)
- `password`: String (hashed)
- `serviceCategory`: String
- `teamMembers`: Array of embedded documents
- `availabilityStatus`: String (online/busy/offline)
- `location`: String

### Jobs Collection
- `_id`: ObjectId
- `jobTitle`: String
- `description`: String
- `location`: String
- `postedBy`: User ID
- `status`: String (pending/accepted/in_progress/completed)
- `assignedVendor`: Vendor ID
- `assignedTeamMember`: String
- `createdAt`: DateTime
- `serviceCategory`: String

## Security

- JWT-based authentication
- Password encryption using BCrypt
- CORS enabled for frontend integration
- Role-based access control (USER/VENDOR)

## Development

To run in development mode with auto-reload:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Testing

Run tests:
```bash
mvn test
```

## Production Deployment

1. Update `application.yml` with production MongoDB URI
2. Change JWT secret to a secure random string
3. Build the application:
   ```bash
   mvn clean package
   ```
4. Run the JAR file:
   ```bash
   java -jar target/vendrconnect-backend-1.0.0.jar
   ```