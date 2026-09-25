# 🏥 HEALTHSYNC — Healthcare Management Platform

A comprehensive Healthcare & Hospital Management Platform built using **Spring Boot**, **Spring Security**, **JWT Authentication**, **Spring Data JPA**, **MySQL**, **Pessimistic Locking**, and a modern web dashboard.

This application provides secure REST APIs for managing patients, doctors, appointments, prescriptions, and real-time dashboard statistics while implementing industry-standard backend development practices.

---

## 🚀 Key Features

### 🔒 Pessimistic Locking & Concurrency Control
* Implements `@Lock(LockModeType.PESSIMISTIC_WRITE)` to prevent double booking.
* Rollback management via `@Transactional` to guarantee database consistency.

### 🔐 Authentication & Authorization
* JWT Token-Based Authentication & Password Reset support
* Spring Security Integration
* Role-Based Access Control (RBAC) — Admin, Doctor, Receptionist
* Secure REST APIs & Custom Validation Messages

### 👨‍⚕️ Doctor Management
* Create Doctor, Update, Delete, View by ID
* Filter Doctors by Specialization
* Role-based access control for staff

### 🧑 Patient Management
* Full CRUD Operations (Create, Read, Update, Delete)
* Search Patients by keyword/name
* Pagination & Sorting Support (`Pageable`, `Sort`)

### 📅 Appointment Management
* Book, Complete, Cancel, and Delete Appointments
* Automated Double-Booking Conflict Prevention
* Appointment Status Tracking (`SCHEDULED`, `COMPLETED`, `CANCELLED`)

### 💊 Prescription Management
* Create, Update, Delete, and Link Prescriptions to Patients & Doctors
* Search Prescriptions by Patient Code or Doctor

### 📊 Real-Time Dashboard
* Live Stats for Total Patients, Doctors, Appointments, Completed, Cancelled, and Prescriptions

---

## 🛠️ Tech Stack

* **Backend**: Java 22, Spring Boot 4, Spring Security, Spring Data JPA, Hibernate ORM
* **Frontend**: HTML5, CSS3, JavaScript (ES6+), Modern SPA Dashboard
* **Database**: MySQL 9.x
* **Security & Auth**: JWT (JSON Web Tokens), BCrypt Password Encoder
* **Documentation**: Swagger UI / OpenAPI 3.0

---

## 💻 Local Setup Instructions

### 1. Clone Repository
```bash
git clone https://github.com/SnehaPatel-24/HEALTHSYNC.git
cd HEALTHSYNC
```

### 2. Configure Database
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hms?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 3. Run Application
```bash
mvnw spring-boot:run
```
Open **http://localhost:8080** in your browser!

---

## 👩‍💻 Author

**Sneha Patel**

Software Developer | Full-Stack & Java Backend Specialist

* **GitHub**: [@SnehaPatel-24](https://github.com/SnehaPatel-24)
* **Project Repository**: [HEALTHSYNC](https://github.com/SnehaPatel-24/HEALTHSYNC)

---

⭐ If you found this project useful, consider giving it a star!
