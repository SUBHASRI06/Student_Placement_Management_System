# Student Placement Management System

A console-based placement management application developed using **Core Java, JDBC, MySQL, and Maven**. The system helps manage student records, company details, placement registrations, eligibility verification, placement status, and basic placement analytics.

## Project Overview

Managing campus placement information manually can make it difficult to track student eligibility, company requirements, registrations, and placement results.

The **Student Placement Management System** provides a centralized console-based application where placement-related information can be stored and managed efficiently.

The application allows users to:

- Add and view student records.
- Add and view company details.
- Check student eligibility based on **CGPA and branch requirements**.
- Register eligible students for companies.
- Prevent duplicate registrations.
- Track and update placement status.
- View placement statistics.
- Export placement information to a **CSV report**.

---

## Technologies Used

- **Java** — Core application development
- **JDBC** — Database connectivity
- **MySQL** — Relational database
- **Maven** — Dependency and project management
- **SQL** — Queries, joins, constraints, and aggregation

---

## Project Architecture

The project follows a layered structure using the **DAO (Data Access Object) Pattern** to separate application logic from database operations.

```text
StudentPlacementSystem/
│
├── src/
│   └── main/
│       └── java/
│           │
│           ├── model/
│           │   ├── Student.java
│           │   ├── Company.java
│           │   └── Placement.java
│           │
│           ├── dao/
│           │   ├── StudentDAO.java
│           │   ├── CompanyDAO.java
│           │   └── PlacementDAO.java
│           │
│           ├── util/
│           │   ├── DatabaseConnection.java
│           │   ├── ValidationUtil.java
│           │   └── ReportUtil.java
│           │
│           └── Main.java
│
├── schema.sql
├── pom.xml
├── .gitignore
└── README.md
```

### Model Layer

Contains Java classes representing the main entities:

- `Student`
- `Company`
- `Placement`

### DAO Layer

Handles database operations using JDBC.

- `StudentDAO` — Student-related database operations
- `CompanyDAO` — Company-related database operations
- `PlacementDAO` — Placement registration and status operations

### Utility Layer

Contains reusable helper classes.

- `DatabaseConnection` — Handles MySQL database connections
- `ValidationUtil` — Performs input validation
- `ReportUtil` — Generates CSV placement reports

---

## ✨ Key Features

### Student Management

Manage student information including:

- Student name
- Branch
- CGPA
- Email
- Phone number

Input validation is performed before storing records in the database.

---

### Company Management

Store placement company information such as:

- Company name
- Minimum required CGPA
- Eligible branch
- Job role
- Package in LPA

---

### Automated Eligibility Verification

Before a student registers for a company, the application verifies:

```text
Student CGPA >= Company Minimum CGPA
```

and checks whether:

```text
Student Branch == Required Branch
```

Companies can also specify `ANY` when students from all branches are eligible.

Only students satisfying the eligibility requirements are allowed to register.

---

### Duplicate Registration Prevention

A student cannot register for the same company more than once.

Duplicate registrations are prevented using both application-level checks and a database `UNIQUE` constraint on:

```text
(student_id, company_id)
```

---

### SQL JOIN Operations

The system uses SQL `JOIN` queries to combine information from the:

- `students`
- `companies`
- `placements`

tables.

This allows placement records to display readable information such as:

```text
Student Name | Company Name | Job Role | Status
```

instead of displaying only database IDs.

---

### Placement Analytics

The system uses SQL aggregate queries and `GROUP BY` operations to display placement statistics such as:

- Number of registered students
- Number of selected students
- Company-wise selection counts

---

### CSV Report Generation

Placement records can be exported to:

```text
placement_report.csv
```

This makes it easier to use placement data for reporting and administrative purposes.

---

## Database Schema

Create the database using the following SQL script.

```sql
CREATE DATABASE IF NOT EXISTS placement_db;

USE placement_db;

CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    branch VARCHAR(50) NOT NULL,
    cgpa DOUBLE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    CHECK (cgpa BETWEEN 0 AND 10)
);

CREATE TABLE IF NOT EXISTS companies (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(100) NOT NULL,
    min_cgpa DOUBLE NOT NULL,
    required_branch VARCHAR(50) NOT NULL,
    job_role VARCHAR(100) NOT NULL,
    package_lpa DOUBLE NOT NULL,
    CHECK (min_cgpa BETWEEN 0 AND 10)
);

CREATE TABLE IF NOT EXISTS placements (
    placement_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    company_id INT NOT NULL,
    status VARCHAR(30) DEFAULT 'Registered',

    FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE,

    FOREIGN KEY (company_id)
        REFERENCES companies(company_id)
        ON DELETE CASCADE,

    CONSTRAINT unique_student_company
        UNIQUE (student_id, company_id)
);
```

---

## Application Menu

When the application starts, the following menu is displayed:

```text
=========================================
   STUDENT PLACEMENT MANAGEMENT SYSTEM
=========================================

1. Add New Student
2. View All Students
3. Add New Company
4. View All Companies
5. Check All Eligible Companies for Student
6. Check Eligibility for Specific Company
7. Apply/Register Student for Company
8. View All Placements
9. Update Placement Status
10. Export Placement Report to CSV
11. View Placement Analytics
12. Exit
```

---

## How to Run the Project

### Prerequisites

Make sure the following are installed:

- JDK 17 or higher
- MySQL Server
- Apache Maven
- Git

---

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/StudentPlacementSystem.git
```

Move into the project directory:

```bash
cd StudentPlacementSystem
```

---

### 2. Create the Database

Open MySQL Workbench or MySQL CLI and execute:

```text
schema.sql
```

Alternatively, manually execute the SQL commands provided in the **Database Schema** section.

---

### 3. Configure Database Connection

Open:

```text
src/main/java/util/DatabaseConnection.java
```

Update the database credentials:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/placement_db";

private static final String USER =
        "your_mysql_username";

private static final String PASSWORD =
        "your_mysql_password";
```

> **Important:** Do not upload your actual MySQL password to a public GitHub repository.

---

### 4. Build the Project

```bash
mvn clean compile
```

---

### 5. Run the Application

Run `Main.java` from your IDE.

If the Maven Exec Plugin is configured, the application can also be executed using:

```bash
mvn exec:java -Dexec.mainClass="Main"
```

---

## Application Workflow

```text
Student Registration
        ↓
Student Profile
        ↓
Company Requirements
        ↓
Eligibility Verification
        ↓
CGPA + Branch Check
        ↓
Eligible?
     /        \
   Yes         No
    ↓           ↓
Register      Reject
    ↓
Placement Record
    ↓
Status Update
    ↓
Analytics / CSV Report
```

---

## Concepts Implemented

This project demonstrates practical implementation of:

- Object-Oriented Programming
- Java Classes and Objects
- Encapsulation
- Exception Handling
- Collections
- JDBC
- PreparedStatement
- ResultSet
- SQL CRUD Operations
- Primary Keys
- Foreign Keys
- Unique Constraints
- SQL JOINs
- SQL Aggregate Functions
- GROUP BY
- DAO Design Pattern
- Input Validation
- File Handling
- CSV Generation
- Maven Project Structure

---

## Data Integrity

The project maintains data integrity using:

- Primary key constraints
- Foreign key relationships
- Unique constraints
- Input validation
- CGPA range validation
- Duplicate registration prevention
- Parameterized SQL queries using `PreparedStatement`

---

## Future Enhancements

Possible future improvements include:

- REST API development using **Spring Boot**
- Web-based frontend using **React**
- Admin, Student, and Recruiter authentication
- Role-based authorization
- Automated email notifications
- Placement drive scheduling
- Student login and application tracking
- Advanced placement analytics
- Resume upload functionality

---

## Purpose of the Project

The project was developed to gain practical experience in building a Java application that integrates **Core Java, JDBC, SQL, relational database design, and layered application architecture** while solving a real-world campus placement management problem.

---
