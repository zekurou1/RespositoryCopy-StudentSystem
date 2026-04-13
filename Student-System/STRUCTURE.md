# Project Structure Guide

This document outlines the complete file structure for the Student Information System.

## Directory Layout

```
Student-System/
├── src/                          # Source code
│   ├── app/
│   │   └── Main.java             # Application entry point
│   ├── config/
│   │   └── DBConnection.java     # Database connection manager
│   ├── model/
│   │   ├── Student.java          # Student POJO
│   │   ├── Course.java           # Course POJO
│   │   └── Enrollment.java       # Enrollment POJO
│   ├── dao/
│   │   ├── StudentDAO.java       # Student data access object
│   │   ├── CourseDAO.java        # Course data access object
│   │   └── EnrollmentDAO.java    # Enrollment data access object
│   ├── service/
│   │   ├── StudentService.java   # Student business logic
│   │   ├── TranscriptService.java # Transcript generation logic
│   │   └── ClassListService.java  # Class list generation logic
│   ├── ui/
│   │   ├── MainFrame.java        # Main application window
│   │   ├── panels/
│   │   │   ├── RegisterStudentPanel.java
│   │   │   ├── EnrollStudentPanel.java
│   │   │   ├── GradePanel.java
│   │   │   ├── TranscriptPanel.java
│   │   │   └── ClassListPanel.java
│   │   └── components/
│   │       ├── TableUtils.java   # Reusable table utilities
│   │       └── FormUtils.java    # Reusable form utilities
│   └── utils/
│       └── Validator.java        # Input validation utilities
│
├── database/
│   ├── schema.sql                # Database table definitions
│   ├── seed.sql                  # Sample data for testing
│   └── queries.sql               # Key SQL queries reference
│
├── lib/
│   └── README.md                 # PostgreSQL JDBC driver instructions
│
└── build/                        # Compiled classes (generated)
    └── (compiled .class files)
```

## Architecture Layers

### 3-Layer Architecture

```
┌─────────────────────────────────────┐
│   Frontend: Java Swing UI Layer     │  (ui.* packages)
│  - MainFrame, Panels, Components    │
└────────────────┬────────────────────┘
                 │ UI calls Service
┌────────────────▼────────────────────┐
│   Backend: Service Layer            │  (service.* packages)
│  - Business logic & calculations    │
└────────────────┬────────────────────┘
                 │ Service calls DAO
┌────────────────▼────────────────────┐
│   Backend: DAO Layer                │  (dao.* packages)
│  - All SQL queries                  │
└────────────────┬────────────────────┘
                 │ DAO uses Connection
┌────────────────▼────────────────────┐
│   Database: PostgreSQL              │
│  - students, courses, enrollments   │
└─────────────────────────────────────┘
```

## Key Rules (Strictly Enforced)

1. **Frontend NEVER writes SQL** - All queries go through DAO
2. **Frontend NEVER connects to database directly** - Use Service layer
3. **Frontend ONLY calls Service methods** - Never call DAO directly
4. **DAO handles ALL SQL queries** - No SQL in Service or UI
5. **Service handles ALL business logic** - Calculations, validation, joins

## File Responsibilities

### Frontend (ui.*)
| File | Responsibility |
|------|-----------------|
| Main.java | Launch application, create MainFrame |
| MainFrame.java | Main window, navigation, panel switching |
| RegisterStudentPanel.java | Student registration form |
| EnrollStudentPanel.java | Student enrollment form |
| GradePanel.java | Grade encoding form |
| TranscriptPanel.java | Transcript display |
| ClassListPanel.java | Class list display |
| TableUtils.java | Reusable JTable creation and formatting |
| FormUtils.java | Reusable form field creation |
| Validator.java | Input validation logic |

### Backend - Models (model.*)
| File | Responsibility |
|------|-----------------|
| Student.java | POJO with student fields |
| Course.java | POJO with course fields |
| Enrollment.java | POJO with enrollment fields |

### Backend - DAO (dao.*)
| File | Responsibility |
|------|-----------------|
| StudentDAO.java | SQL for students table |
| CourseDAO.java | SQL for courses table |
| EnrollmentDAO.java | SQL for enrollments table |

### Backend - Service (service.*)
| File | Responsibility |
|------|-----------------|
| StudentService.java | Wraps StudentDAO for UI |
| TranscriptService.java | TOR JOIN query & processing |
| ClassListService.java | ClassList JOIN query & summary |

### Backend - Configuration (config.*)
| File | Responsibility |
|------|-----------------|
| DBConnection.java | PostgreSQL JDBC connection |

## Database Setup

### 1. Create Database Tables
Execute `database/schema.sql` in PostgreSQL:
```sql
CREATE TABLE students (...)
CREATE TABLE courses (...)
CREATE TABLE enrollments (...)
```

### 2. Load Sample Data
Execute `database/seed.sql` in PostgreSQL:
```sql
INSERT INTO students ...
INSERT INTO courses ...
INSERT INTO enrollments ...
```

### 3. Reference Queries
All key queries are documented in `database/queries.sql`

## Build & Compilation

### Compile from Command Line
```bash
# Compile with PostgreSQL JDBC driver
javac -d build -cp "lib/postgresql-*.jar" src/**/*.java

# Run application
java -cp "build:lib/postgresql-*.jar" app.Main
```

### IDE Setup (Eclipse, IntelliJ)
1. Add PostgreSQL JDBC driver to project classpath
2. Set source folder: `src/`
3. Set output folder: `build/`
4. Run class: `app.Main`

## Feature Implementation Order

Recommended build order based on dependencies:

1. **Phase 1 (Parallel):**
   - BE1: DBConnection, schema, seed, queries, PostgreSQL jar
   - FE1: Main.java, MainFrame.java

2. **Phase 2:**
   - BE2: Model classes, DAO classes, StudentService

3. **Phase 3 (Parallel):**
   - BE3: TranscriptService, ClassListService
   - FE2: RegisterPanel, EnrollPanel, GradePanel, FormUtils, Validator

4. **Phase 4:**
   - FE3: TranscriptPanel, ClassListPanel, TableUtils

## TODO Checklist

Each Java file contains TODO comments marking what needs to be implemented:
- [ ] All database files (schema, seed, queries)
- [ ] All model classes (Student, Course, Enrollment)
- [ ] All DAO classes (StudentDAO, CourseDAO, EnrollmentDAO)
- [ ] All Service classes (StudentService, TranscriptService, ClassListService)
- [ ] Main.java entry point
- [ ] MainFrame.java window setup
- [ ] All Panel classes (5 panels)
- [ ] Utility classes (FormUtils, TableUtils, Validator)
- [ ] DBConnection configuration

## Common Implementation Patterns

### Service → DAO Call Pattern
```java
// In Service class
public void insertStudent(...) throws Exception {
    Student student = new Student(...);  // Create model
    studentDAO.insertStudent(student);     // Call DAO
}
```

### Frontend → Service Call Pattern
```java
// In Panel class
StudentService service = new StudentService();
service.insertStudent(name, address, birthdate, ...);  // Call service
```

### DAO → Database Pattern
```java
// In DAO class
Connection conn = DBConnection.getConnection();
PreparedStatement ps = conn.prepareStatement("SQL query");
ps.setString(1, value);
ps.executeUpdate();
```

---

**All files are ready for implementation. Start with database setup, then follow the build order.**
