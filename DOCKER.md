# Docker Deployment Guide

This guide explains how to run the User Management Service using Docker.

---

## Prerequisites

You need to have Docker installed on your system:

**Docker Desktop (Recommended for Windows/Mac):**
- Download from: https://www.docker.com/products/docker-desktop

**Docker Engine (Linux):**
- Install instructions: https://docs.docker.com/engine/install/

**Verify Installation:**

```bash
docker --version
# Expected: Docker version 20.x.x or higher

docker-compose --version
# Expected: Docker Compose version 2.x.x or higher
```

---

## Quick Start with Docker Compose (Easiest Method)

This is the simplest way to run the application.

### Step 1: Clone the Repository

```bash
git clone https://github.com/HamidCodeHub/user-management-service.git
cd user-management-service
```

### Step 2: Run with Docker Compose

```bash
docker-compose up -d
```

This command will:
1. Build the Docker image (first time only, takes a few minutes)
2. Start the container in the background (`-d` = detached mode)
3. Map port 8080 to your local machine

### Step 3: Verify It's Running

```bash
# Check container status
docker-compose ps

# View logs
docker-compose logs -f user-management-service
```

You should see:
```
user-management-service is up and running on port 8080
```

### Step 4: Access the Application

Open your browser:
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **H2 Console:** http://localhost:8080/h2-console
- **Health Check:** http://localhost:8080/actuator/health

### Step 5: Stop the Application

```bash
# Stop containers
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

---

## Building the Docker Image Manually

If you want to build the Docker image without docker-compose:

### Build the Image

```bash
docker build -t user-management-service:1.0.0 .
```

This creates an image named `user-management-service` with tag `1.0.0`.

**Build Progress:**

```
[+] Building 45.2s (15/15) FINISHED
 => [internal] load build definition
 => [internal] load .dockerignore
 => [build 1/6] FROM maven:3.9.9-eclipse-temurin-17-alpine
 => [build 2/6] WORKDIR /app
 => [build 3/6] COPY .mvn/ .mvn/
 => [build 4/6] COPY mvnw pom.xml ./
 => [build 5/6] RUN ./mvnw dependency:go-offline -B
 => [build 6/6] COPY src ./src
 => [build 7/6] RUN ./mvnw clean package -DskipTests
 => [stage-1 1/5] FROM eclipse-temurin:17-jre-alpine
 => [stage-1 2/5] RUN addgroup -S spring && adduser -S spring -G spring
 => [stage-1 3/5] WORKDIR /app
 => [stage-1 4/5] COPY --from=build /app/target/*.jar app.jar
 => [stage-1 5/5] RUN chown -R spring:spring /app
 => exporting to image
 => => naming to docker.io/library/user-management-service:1.0.0
```

### Run the Container

```bash
docker run -d \
  --name user-management-service \
  -p 8080:8080 \
  user-management-service:1.0.0
```

**Explanation:**
- `-d`: Run in background (detached mode)
- `--name`: Give the container a friendly name
- `-p 8080:8080`: Map port 8080 from container to host
- `user-management-service:1.0.0`: The image to run

### Check Container Status

```bash
# List running containers
docker ps

# View container logs
docker logs user-management-service

# Follow logs in real-time
docker logs -f user-management-service

# View last 100 lines
docker logs --tail 100 user-management-service
```

### Stop and Remove Container

```bash
# Stop the container
docker stop user-management-service

# Remove the container
docker rm user-management-service

# Do both at once
docker rm -f user-management-service
```

---

## Docker Image Details

### Multi-Stage Build

The Dockerfile uses a multi-stage build for optimization:

**Stage 1: Build (Maven + JDK 17)**
- Uses Maven to compile and package the application
- Downloads all dependencies
- Creates the JAR file

**Stage 2: Runtime (JRE 17 Alpine)**
- Uses lightweight Alpine Linux base image
- Only includes Java Runtime Environment (not full JDK)
- Copies only the JAR file from Stage 1
- Results in much smaller final image (~200MB vs ~600MB)

### Security Features

1. **Non-root User:**
   - Application runs as user `spring` (not root)
   - Better security practice

2. **Minimal Base Image:**
   - Alpine Linux is minimal and secure
   - Less attack surface

3. **Health Checks:**
   - Built-in health monitoring
   - Docker can automatically restart unhealthy containers

### Image Size Comparison

```bash
# View image size
docker images user-management-service

REPOSITORY                 TAG       SIZE
user-management-service    1.0.0     ~250MB
```

Compare to alternatives:
- With full JDK: ~600MB
- Without multi-stage build: ~800MB
- Our optimized image: ~250MB ✅

---

## Docker Compose Configuration

### Basic Usage

**Start services:**
```bash
docker-compose up -d
```

**Stop services:**
```bash
docker-compose down
```

**View logs:**
```bash
docker-compose logs -f
```

**Restart services:**
```bash
docker-compose restart
```

**Rebuild and restart:**
```bash
docker-compose up -d --build
```

### Environment Variables

You can customize the application by setting environment variables in `docker-compose.yml`:

```yaml
environment:
  - SERVER_PORT=8080
  - SPRING_PROFILES_ACTIVE=docker
  - JAVA_OPTS=-Xms256m -Xmx512m
```

Or pass them via command line:

```bash
docker-compose up -d -e SERVER_PORT=9090
```

### Resource Limits

The docker-compose.yml includes resource limits:

```yaml
deploy:
  resources:
    limits:
      cpus: '1.0'        # Maximum 1 CPU core
      memory: 768M       # Maximum 768MB RAM
    reservations:
      cpus: '0.5'        # Minimum 0.5 CPU cores
      memory: 512M       # Minimum 512MB RAM
```

Adjust these based on your needs.

---

## Using PostgreSQL Instead of H2

The default configuration uses H2 (in-memory database), but you can easily switch to PostgreSQL for production use.

### Step 1: Enable PostgreSQL in docker-compose.yml

Uncomment the PostgreSQL service:

```yaml
postgres:
  image: postgres:16-alpine
  container_name: user-management-postgres
  environment:
    - POSTGRES_DB=userdb
    - POSTGRES_USER=admin
    - POSTGRES_PASSWORD=admin123
  ports:
    - "5432:5432"
  volumes:
    - postgres-data:/var/lib/postgresql/data
  networks:
    - app-network

volumes:
  postgres-data:
    driver: local
```

### Step 2: Update Application Configuration

In `src/main/resources/application-docker.yml`, uncomment PostgreSQL configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/userdb
    driver-class-name: org.postgresql.Driver
    username: admin
    password: admin123
  
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

### Step 3: Add PostgreSQL Driver

Add dependency in `pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Step 4: Restart Services

```bash
docker-compose down
docker-compose up -d --build
```

Now your application uses PostgreSQL with persistent data!

---

## Debugging and Troubleshooting

### View Container Logs

```bash
# All logs
docker-compose logs user-management-service

# Last 100 lines
docker-compose logs --tail=100 user-management-service

# Follow logs (real-time)
docker-compose logs -f user-management-service

# Logs with timestamps
docker-compose logs -t user-management-service
```

### Execute Commands Inside Container

```bash
# Open a shell inside the container
docker exec -it user-management-service sh

# Check Java version inside container
docker exec user-management-service java -version

# Check running processes
docker exec user-management-service ps aux
```

### Inspect Container

```bash
# View detailed container information
docker inspect user-management-service

# View container IP address
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' user-management-service

# View environment variables
docker inspect -f '{{json .Config.Env}}' user-management-service
```

### Health Check Status

```bash
# View health status
docker inspect --format='{{json .State.Health}}' user-management-service | jq

# Expected healthy output:
{
  "Status": "healthy",
  "FailingStreak": 0,
  "Log": [...]
}
```

### Common Issues

**Issue 1: Port already in use**

Error:
```
Error starting userland proxy: listen tcp4 0.0.0.0:8080: bind: address already in use
```

Solution:
```bash
# Find process using port 8080
netstat -ano | findstr :8080  # Windows
lsof -i :8080                  # Linux/Mac

# Kill the process or change port in docker-compose.yml
ports:
  - "8081:8080"  # Map to different host port
```

**Issue 2: Build fails**

Solution:
```bash
# Clean Docker build cache
docker-compose build --no-cache

# Or rebuild from scratch
docker-compose down
docker system prune -a
docker-compose up -d --build
```

**Issue 3: Container keeps restarting**

Check logs for errors:
```bash
docker-compose logs user-management-service

# Common causes:
# - Application crashes on startup
# - Wrong configuration
# - Health check failing
```

**Issue 4: Cannot connect to application**

Check container is running:
```bash
docker-compose ps

# Should show:
NAME                        STATUS
user-management-service     Up (healthy)
```

Test from inside container:
```bash
docker exec user-management-service wget -O- http://localhost:8080/actuator/health
```

---

## Production Deployment Tips

### 1. Use Tagged Versions

Don't use `latest` tag in production:

```bash
docker build -t user-management-service:1.0.0 .
docker tag user-management-service:1.0.0 myregistry/user-management-service:1.0.0
```

### 2. Use External Database

Never use H2 in production. Switch to PostgreSQL or MySQL.

### 3. Configure Secrets Properly

Don't hardcode passwords in docker-compose.yml. Use environment files:

```bash
# Create .env file (don't commit to git!)
POSTGRES_PASSWORD=secure_password_here
DATABASE_URL=jdbc:postgresql://db:5432/userdb
```

```yaml
# docker-compose.yml
environment:
  - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
  - SPRING_DATASOURCE_URL=${DATABASE_URL}
```

### 4. Set Resource Limits

Always set memory and CPU limits in production:

```yaml
deploy:
  resources:
    limits:
      memory: 1G
      cpus: '2.0'
```

### 5. Use Proper Logging

Configure log rotation:

```yaml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

### 6. Enable HTTPS

Use a reverse proxy (nginx, traefik) in front of your container for SSL/TLS.

### 7. Monitor Health

Use the health check endpoint:
```
http://your-domain/actuator/health
```

Integrate with monitoring tools (Prometheus, Grafana, etc.)

---

## Docker Commands Cheat Sheet

**Images:**
```bash
docker images                              # List all images
docker build -t name:tag .                 # Build image
docker rmi image_name                      # Remove image
docker image prune                         # Remove unused images
```

**Containers:**
```bash
docker ps                                  # List running containers
docker ps -a                               # List all containers
docker run -d -p 8080:8080 image_name     # Run container
docker stop container_name                 # Stop container
docker start container_name                # Start stopped container
docker restart container_name              # Restart container
docker rm container_name                   # Remove container
docker logs container_name                 # View logs
docker exec -it container_name sh         # Open shell
```

**Docker Compose:**
```bash
docker-compose up -d                       # Start services
docker-compose down                        # Stop services
docker-compose logs -f                     # View logs
docker-compose ps                          # List services
docker-compose restart                     # Restart services
docker-compose build                       # Build images
docker-compose up -d --build              # Rebuild and restart
```

**System:**
```bash
docker system df                           # Show disk usage
docker system prune                        # Clean up
docker system prune -a --volumes          # Deep clean (careful!)
```

---

## Next Steps

1. **Push to Docker Hub:**
   ```bash
   docker login
   docker tag user-management-service:1.0.0 yourusername/user-management-service:1.0.0
   docker push yourusername/user-management-service:1.0.0
   ```

2. **Deploy to Cloud:**
   - AWS ECS
   - Google Cloud Run
   - Azure Container Instances
   - Kubernetes

3. **Add CI/CD:**
   - Automate Docker builds with GitHub Actions
   - Auto-deploy on push to main branch

---

## Support

For Docker-related issues:
- Docker Documentation: https://docs.docker.com
- Docker Community: https://forums.docker.com
- Project Issues: https://github.com/HamidCodeHub/user-management-service/issues

---

**Last Updated:** February 2026
