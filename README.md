# Enhanced Spring Boot CRM System Backend Architecture with Enums

## Core Architecture Principles

### 1. Modular Design (Loose Coupling)
- **Multi-Module Maven/Gradle Project**: Each department as separate module
- **Spring Boot Starter Pattern**: Core functionality as auto-configuration
- **Event-Driven Communication**: Spring Application Events for inter-module communication
- **Conditional Bean Loading**: Modules activated based on configuration

### 2. Role-Based Access Control (RBAC)
- **Spring Security**: Core authentication and authorization
- **Method-Level Security**: Fine-grained access control with annotations
- **Hierarchical Permissions**: Custom permission evaluator for inheritance
- **Dynamic Permission Loading**: Real-time permission evaluation

### 3. Action-Based UI Generation
- **Custom Security Annotations**: Return available actions per user/resource
- **REST Controllers**: Clean API for frontend integration

## Project Structure

```
crm-system/
├── crm-core/                          # Core module
│   ├── crm-security/                  # Security & RBAC
│   ├── crm-common/                    # Common utilities & enums
│   └── crm-api/                       # API Gateway
├── crm-modules/                       # Business modules
│   ├── crm-sales/                     # Sales module
│   ├── crm-hr/                        # HR module
│   ├── crm-finance/                   # Finance module
│   ├── crm-it/                        # IT module
│   ├── crm-marketing/                 # Marketing module
│   └── crm-operations/                # Operations module
├── crm-starter/                       # Spring Boot starter
└── crm-application/   