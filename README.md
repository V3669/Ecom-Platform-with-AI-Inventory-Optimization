# E-commerce Platform with AI Inventory Optimization

## 🎯 Project Vision
Building a modern, scalable e-commerce platform with intelligent inventory management powered by AI. The platform uses microservices architecture and implements an AI-driven system to optimize inventory levels based on order trends and patterns.

## 🏗️ Current Status
- ✅ API Gateway (Port 8080)
  - JWT Authentication & Authorization
  - Request Logging & Monitoring
  - Dynamic Route Management
  - Circuit Breaking & Load Balancing
  - Global Exception Handling
  
- ✅ User Service (Port 8081)
  - User Authentication & Authorization
  - User Profile Management
  - JWT Token Generation
  - Role-based Access Control

- ✅ Inventory Service (Port 8082)
  - Product Management
  - Stock Level Tracking
  - Real-time Inventory Updates
  - Event-Based Stock Reservation
  - Low Stock Alerts
  
- ✅ Order Service (Port 8083)
  - Event-Driven Architecture with RabbitMQ
  - Order Management REST APIs
  - Order Status Tracking
  - Event Publishing/Listening

- ✅ AI Service (Port 8084)
  - Inventory Optimization with Prophet model
  - Daily & Monthly Trend Detection
  - Future Demand Forecasting
  - Safety Stock Calculation
  - Stock Recommendation Engine
  - Event-Driven Integration with Inventory Service
  - Includes Prophet Python Microservice (Port 5000)

## 🎯 Implementation Plan

### Phase 1: Core Services (Completed)
- [x] API Gateway Setup
- [x] User Service Implementation
- [x] Inventory Service Implementation
- [x] Order Service Implementation
- [x] AI Service Integration
- [x] Basic Inventory Optimization

### Phase 2: AI Enhancement (Current)
- [ ] Multi-Model Approach Integration
- [ ] External Trend API Integration
- [ ] Customer Behavior Analysis
- [ ] Category-Specific Optimization

### Phase 3: Future Enhancements (Planned)
- [ ] Real-time Anomaly Detection
- [ ] Price Optimization
- [ ] Promotional Impact Analysis
- [ ] Supplier & Logistics Optimization

## 🤖 AI Implementation Details

### Current Scope (MVP)
```
Single Model Implementation
├── Input Features
│   ├── Daily Order Volumes
│   ├── Weekly Trends
│   └── Product Categories
│
├── Output
│   ├── Demand Forecast
│   └── Inventory Recommendations
│
└── Retraining Schedule
    └── Weekly Updates
```

### Prophet AI Service
The AI Service uses Facebook's Prophet, a powerful time series forecasting model, to predict future demand and optimize inventory levels. It implements a hybrid architecture:

```
AI Service Architecture
├── Java Spring Boot Service (Port 8084)
│   ├── RESTful API Endpoints
│   ├── Inventory Optimization Logic
│   ├── Event Handling & Processing
│   └── Integration with Inventory Service
│
└── Python Prophet Service (Port 5000)
    ├── Time Series Forecasting
    ├── Trend Analysis
    ├── Seasonality Detection
    └── Future Demand Prediction
```

#### Prophet Service API Endpoints

##### Forecast Demand
```
POST /api/forecast
```
Forecasts future demand based on historical data.

**Request Body:**
```json
{
  "historical_data": [
    {"date": "2023-01-01", "quantity": 10},
    {"date": "2023-01-02", "quantity": 12}
  ],
  "days_to_forecast": 30
}
```

**Response:**
```json
{
  "2023-02-01": 15.2,
  "2023-02-02": 16.7
}
```

##### Trend Analysis
```
POST /api/trends
```
Extracts trend components from historical data.

**Request:**
```json
{
  "historical_data": [
    {"date": "2023-01-01", "quantity": 10},
    {"date": "2023-01-02", "quantity": 12}
  ]
}
```

##### Seasonality Analysis
```
POST /api/seasonality
```
Extracts seasonal patterns from historical data.

### Future Vision
```
Multi-Model Architecture
├── Core Models
│   ├── Demand Forecasting
│   ├── Price Optimization
│   └── Customer Segmentation
│
├── Specialized Models
│   ├── Seasonal Trends
│   ├── Category-Specific
│   └── Geographic Patterns
│
└── Retraining Schedule
    ├── Monthly Base Updates
    └── Quarterly Deep Learning
```

## 🏛️ Architecture Overview

```
┌─────────────────┐
│   API Gateway   │
└───────┬─────────┘
        │
        ▼
┌─────────────┬─────────────┬─────────────┐
│             │             │             │
▼             ▼             ▼             ▼
┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐
│  User   │  │Inventory│  │  Order  │  │   AI    │
│ Service │  │Service│◀─▶│ Service │  │ Service │
└─────────┘  └────┬────┘  └────┬────┘  └────┬────┘
                  │            │            │
                  └────────────┴────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   Event Bus     │
                  │   (RabbitMQ)    │
                  └─────────────────┘
```

## 🔄 Event-Driven Architecture

Our platform uses event-driven communication through RabbitMQ to enable loose coupling between microservices while maintaining data consistency.

### How It Works
- **Asynchronous Communication**: Services communicate by publishing events to RabbitMQ and subscribing to relevant events
- **Decoupled Services**: Each microservice operates independently, unaware of other services' implementations
- **Fault Tolerance**: Failed operations can be retried without breaking the entire workflow
- **Scalability**: Services can be scaled independently based on their specific workload

### Core Event Flows

**Order Processing Flow**
1. API Gateway routes order request to Order Service
2. Order Service creates order and publishes `ORDER_CREATED` event
3. Inventory Service receives event, reserves stock, publishes `STOCK_RESERVED`
4. Order Service receives `STOCK_RESERVED`, updates order to "CONFIRMED"
5. Payment processing occurs, resulting in `PAYMENT_COMPLETED` event
6. Inventory Service receives payment event and finalizes stock updates

**Inventory Management Flow**
1. Inventory Service detects low stock and publishes `LOW_STOCK_ALERT`
2. AI Service analyzes data and returns `STOCK_RECOMMENDATION`
3. Inventory Service uses AI recommendations for stock planning

### Key Event Types
- **Order Events**: `ORDER_CREATED`, `ORDER_CONFIRMED`, `ORDER_CANCELLED`
- **Inventory Events**: `STOCK_RESERVED`, `STOCK_RELEASED`, `LOW_STOCK_ALERT` 
- **Payment Events**: `PAYMENT_COMPLETED`, `PAYMENT_FAILED`
- **AI Events**: `STOCK_RECOMMENDATION`, `DEMAND_FORECAST`

### Benefits
- **Resilience**: System continues to function even if some services are temporarily unavailable
- **Real-time Processing**: Events are processed as they occur
- **Audit Trail**: Complete history of system actions is maintained
- **Extensibility**: New services can be added without modifying existing ones

## 🛠️ Technical Stack

- **Backend**: Spring Boot, Java 17
- **Message Broker**: RabbitMQ
- **Databases**: 
  - PostgreSQL (Users, Orders, Products)
  - Redis (Cache, Real-time Inventory)
- **AI/ML**: 
  - Python, Facebook Prophet for time series forecasting
  - Spring WebFlux for reactive API calls
- **Authentication**: JWT
- **API Gateway**: Spring Cloud Gateway
- **Containerization**: Docker, Docker Compose

## 📊 Current Metrics & Goals

### MVP Phase
- Order Processing: < 100ms
- AI Prediction Time: < 200ms
- Inventory Accuracy: > 85%

### Production Goals
- Order Processing: < 50ms
- AI Prediction Time: < 100ms
- Inventory Accuracy: > 95%

## 🚀 Getting Started

### Prerequisites
- Java 17
- PostgreSQL
- Redis
- RabbitMQ
- Python 3.10+
- Docker & Docker Compose (optional)

### Configuration

```yaml
# API Gateway (Port 8080)
spring:
  cloud:
    gateway:
      routes:
        - user-service: localhost:8081
        - inventory-service: localhost:8082
        - order-service: localhost:8083
        - ai-service: localhost:8084

# User Service (Port 8081)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecom_users
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration: 86400000 # 24 hours

# Inventory Service (Port 8082)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecom_inventory
  redis:
    host: localhost
    port: 6379
  rabbitmq:
    host: localhost
    port: 5672

# Order Service (Port 8083)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecom_orders
  rabbitmq:
    host: localhost
    port: 5672

# AI Service (Port 8084)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecom_ai
  rabbitmq:
    host: localhost
    port: 5672
  prophet:
    service:
      url: ${PROPHET_SERVICE_URL:http://localhost:5000}
```

### Running the Services

#### Using Docker Compose

```bash
# Build and start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f
```

#### Manual Setup
1. Start PostgreSQL
2. Start Redis
3. Start RabbitMQ
4. Start Python Prophet Service
   ```bash
   cd ai-service/prophet-service
   pip install -r requirements.txt
   python app.py
   ```
5. Launch API Gateway
6. Launch User Service
7. Launch Inventory Service
8. Launch Order Service
9. Launch AI Service

## 📝 Contributing

Hey there! 👋 I'd love your help to make this project even better! Here's how you can jump in:

### How to Contribute
- **Spot a Bug?** 🐛 Let me know! Open an issue and I'll check it out.
- **Got Ideas?** 💡 If you have a cool feature in mind, I want to hear it!
- **Code Time!** 💻 Feel free to fork the repo, make your changes, and send a pull request. Just keep it clean and include any tests if you can.
- **Docs Help?** 📝 If you see something that could use a little love in the docs, go for it!

### Getting Started
1. **Fork It!** Hit that "Fork" button up top.
2. **Clone Your Fork**: `git clone <your-fork-url>`
3. **Create a Branch**: `git checkout -b your-branch-name`
4. **Make Your Changes**: Do your thing!
5. **Push It**: `git push origin your-branch-name`
6. **Pull Request Time!** Go back to the original repo and hit "New Pull Request".

### Let's Connect!
Feel free to reach out if you have questions or just want to chat about the project. Thanks for considering contributing! You rock! 🤘

## 📜 License
This project is licensed under the MIT License - see the LICENSE file for details.  