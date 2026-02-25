# 🛒 E-Commerce Backend System

A comprehensive e-commerce backend built with Spring Boot, Spring Data JPA, and PostgreSQL. This system demonstrates advanced database integration patterns including complex entity relationships, transaction management, and performance optimization.

## 🏗️ Architecture Overview

### Core Technologies
- **Spring Boot 3.2** - Application framework
- **Spring Data JPA** - Database access layer
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations
- **HikariCP** - Connection pooling
- **Lombok** - Boilerplate reduction

### Database Design
The system implements a complete e-commerce schema with the following entities:

```
Users ↔ Orders ↔ OrderItems ↔ Products ↔ Categories
              ↘ Payments
```

**Key Relationships:**
- One-to-Many: User → Orders
- One-to-Many: Order → OrderItems  
- Many-to-One: OrderItem → Product
- Many-to-One: Product → Category
- One-to-One: Order ↔ Payment

## 🚀 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL database
- Maven 3.8+

### Database Setup
1. Create PostgreSQL database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080` and automatically:
- Execute Flyway migrations
- Initialize sample data
- Start the REST API server

## 📊 API Endpoints

### Product Management
```
GET    /api/products              - List all products (paginated)
GET    /api/products/{id}         - Get product details
GET    /api/products/category/{id} - Get products by category
GET    /api/products/search       - Search products by name
GET    /api/products/price-range  - Filter products by price range
GET    /api/products/low-stock    - Get low stock products
POST   /api/products              - Create new product
PUT    /api/products/{id}         - Update product
DELETE /api/products/{id}         - Delete product
PUT    /api/products/{id}/deactivate - Deactivate product
```

### Category Management
```
GET    /api/categories            - List all categories
GET    /api/categories/{id}       - Get category details
GET    /api/categories/root       - Get root categories
GET    /api/categories/{id}/subcategories - Get subcategories
POST   /api/categories            - Create new category
PUT    /api/categories/{id}       - Update category
DELETE /api/categories/{id}       - Delete category
```

### Order Management
```
GET    /api/orders                - List all orders (paginated)
GET    /api/orders/{id}           - Get order details with items
GET    /api/orders/user/{userId}  - Get user orders
GET    /api/orders/status/{status} - Get orders by status
GET    /api/orders/order-number/{number} - Get order by number
GET    /api/orders/report/daily   - Get daily order report
POST   /api/orders                - Create new order
PUT    /api/orders/{id}/status    - Update order status
PUT    /api/orders/{id}/cancel    - Cancel order
DELETE /api/orders/{id}           - Delete order
```

### Payment Management
```
GET    /api/payments              - List all payments
GET    /api/payments/{id}         - Get payment details
GET    /api/payments/order/{orderId} - Get payment by order
GET    /api/payments/status/{status} - Get payments by status
GET    /api/payments/report       - Get payment report
GET    /api/payments/statistics   - Get payment statistics
GET    /api/payments/revenue      - Get total revenue
POST   /api/payments              - Create new payment
PUT    /api/payments/{id}/process - Process payment
PUT    /api/payments/{id}/fail    - Fail payment
DELETE /api/payments/{id}         - Delete payment
```

## 🔧 Key Features

### Transaction Management
- **@Transactional** annotations ensure data consistency
- Order processing includes stock validation and updates
- Payment processing with order status synchronization
- Automatic rollback on failures

### Performance Optimization
- **Connection Pooling**: HikariCP with optimized settings
- **Database Indexes**: Strategic indexing on frequently queried columns
- **Lazy Loading**: Proper fetch strategies to avoid N+1 problems
- **Query Optimization**: Custom JPQL and native queries for complex operations

### Data Integrity
- **Foreign Key Constraints**: Enforced at database level
- **Validation**: Entity-level and service-level validation
- **Auditing**: Created/updated timestamps and user tracking
- **Unique Constraints**: Prevents duplicate critical data

### Migration Management
- **Flyway**: Version-controlled database schema changes
- **Baseline Support**: Handles existing databases
- **Rollback Capability**: Structured migration approach

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

Tests include:
- Repository layer integration tests
- Service layer unit tests
- Entity relationship validation
- Transaction boundary testing

## 📈 Sample Data

The application automatically seeds the database with:
- **Users**: Sample customer accounts
- **Categories**: Electronics, Clothing, Books, Home & Garden
- **Products**: Various products across categories
- **Orders**: Sample order data (can be added manually)

## 🔍 Monitoring & Debugging

### Logging Configuration
```properties
# SQL query logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Transaction logging
logging.level.org.springframework.transaction=DEBUG
```

### Health Check
```
GET /actuator/health
```

## 🛠️ Development Features

### Code Quality
- **Lombok**: Reduces boilerplate code
- **Builder Pattern**: Clean object creation
- **Method Chaining**: Fluent API design
- **Exception Handling**: Proper error responses

### Best Practices Implemented
- **Repository Pattern**: Clean data access layer
- **Service Layer**: Business logic separation
- **DTO Pattern**: Data transfer objects (can be extended)
- **RESTful Design**: Standard HTTP methods and status codes

## 📚 Learning Objectives Covered

✅ **Relational Database Fundamentals** - Tables, relationships, constraints
✅ **JPA/Hibernate ORM** - Entity mapping, relationships, lifecycle
✅ **Spring Data JPA** - Repository pattern, query methods, @Query
✅ **Transaction Management** - @Transactional, ACID properties
✅ **Database Migrations** - Flyway schema versioning
✅ **Performance Optimization** - Indexing, connection pooling, query tuning
✅ **API Design** - RESTful endpoints, pagination, filtering
✅ **Testing** - Integration tests, data validation

## 🎯 Sample Usage

### Create an Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "user": {"id": 1},
    "orderItems": [
      {
        "product": {"id": 1},
        "quantity": 2
      }
    ],
    "shippingAddress": "123 Main St, City, Country"
  }'
```

### Search Products
```bash
curl "http://localhost:8080/api/products/search?keyword=iPhone&page=0&size=5"
```

### Get Order Report
```bash
curl "http://localhost:8080/api/orders/report/daily?startDate=2024-01-01T00:00:00"
```

## 📝 Next Steps

Potential enhancements:
- Add authentication and authorization (JWT/Spring Security)
- Implement caching layer (Redis)
- Add event-driven architecture
- Include comprehensive API documentation (Swagger)
- Add more advanced reporting features
- Implement microservices architecture

---

**Built with ❤️ using Spring Boot and PostgreSQL**