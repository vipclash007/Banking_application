# Banking Application

This project is a secure, scalable distributed banking application.
- **Java RMI (Remote Method Invocation)** for secure communication between client and server.
- **Hibernate ORM** for persistent data storage and management of account data.

### Features

- Remote access to bank accounts.
- View account balance.
- Deposit and withdraw funds.
- Persistent storage of account data using Hibernate.

---

## 🏗️ Architecture

This is a **client-server** application with the following components:

- **Client**: GUI or CLI to request banking operations (e.g., view balance, deposit, withdraw).
- **Server**: Hosts the RMI registry, exposes remote methods, and handles business logic.
- **Database Layer**: Uses Hibernate to manage account data in the backend database.

---

## 🔧 Technologies Used

- **Java RMI**
- **Hibernate ORM**
- **MySQL** (or any compatible RDBMS)
- **JDK 8+**
- **Maven/Gradle** (for dependency management)

---

## 🚀 Getting Started

### Prerequisites

- JDK 8 or higher
- MySQL or any supported RDBMS
- Maven or Gradle (optional but recommended)

### Setup Instructions

1. *Clone the repository*
2. *Start the RMI Registry*
3. *Run the Server* :
     Compile and start the server to bind remote objects to the RMI registry.

4. *Run the Client* :
     Connects to the RMI registry and invokes methods on the remote server.

5. *Database Configuration* :
     Configure hibernate.cfg.xml with your database connection details.


