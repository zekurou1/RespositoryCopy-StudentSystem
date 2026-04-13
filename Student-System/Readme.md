# Student Information System

A Java Swing-based Student Information System with a 3-layer architecture (Frontend, Service, DAO layers) and PostgreSQL database.

## Project Overview

- **Frontend**: Java Swing desktop GUI
- **Backend**: Service and DAO layers for business logic and data access
- **Database**: PostgreSQL with JDBC
- **Architecture**: Strictly enforced 3-layer architecture (no SQL in UI, no direct DB connections from UI)

## Features

1. **Register Student** - Add new students to the system
2. **Enroll Student to Course** - Assign students to courses
3. **Encode Grade** - Set grades for student enrollments
4. **View Transcript of Records (TOR)** - Display student's academic record
5. **View Class List** - Display course roster with summary statistics

## Project Structure

```
Student-System/
├── src/                          # Java source code
│   ├── app/                      # Entry point
│   ├── config/                   # Database configuration
│   ├── model/                    # POJO models (Student, Course, Enrollment)
│   ├── dao/                      # Data Access Objects (SQL queries)
│   ├── service/                  # Business logic and services
│   ├── ui/                       # Main GUI frame
│   │   ├── panels/              # Feature panels (5 panels)
│   │   └── components/          # Reusable UI components
│   └── utils/                    # Utilities (Validator)
├── database/                     # Database files
│   ├── schema.sql               # Table definitions
│   ├── seed.sql                 # Sample data
│   └── queries.sql              # Reference queries
├── lib/                          # External libraries (PostgreSQL JDBC)
├── build/                        # Compiled classes (generated)
└── STRUCTURE.md                  # Detailed structure guide
```

## Architecture Rules (Strictly Enforced)

```
[Java Swing UI] → [Service Layer] → [DAO Layer] → [PostgreSQL Database]
```

### Key Rules:
1. **Frontend NEVER writes SQL** - All queries must go through DAO
2. **Frontend NEVER connects to database directly** - Use Service layer
3. **Frontend ONLY calls Service methods** - Never call DAO directly
4. **DAO handles ALL SQL queries** - No SQL in Service or UI layers
5. **Service handles ALL business logic** - Calculations, validation, joins

## Setup Instructions

### 1. Prerequisites
- Java Development Kit (JDK) 8 or higher
- PostgreSQL 11 or higher
- PostgreSQL JDBC Driver

### 2. Database Setup

#### a) Create PostgreSQL database
```sql
CREATE DATABASE student_system;
```

#### b) Create tables
Execute `database/schema.sql`:
```bash
psql -U postgres -d student_system -f database/schema.sql
```

#### c) Load sample data (optional)
Execute `database/seed.sql`:
```bash
psql -U postgres -d student_system -f database/seed.sql
```

### 3. Download PostgreSQL JDBC Driver

1. Download from: https://jdbc.postgresql.org/download.html
2. Place the JAR file (e.g., `postgresql-42.7.1.jar`) in the `lib/` directory

### 4. Configure Database Connection

Edit `src/config/DBConnection.java`:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/student_system";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

### 5. Compile and Run

#### Using Command Line:
```bash
# Compile
javac -d build -cp "lib/postgresql-*.jar" src/**/*.java

# Run
java -cp "build:lib/postgresql-*.jar" app.Main
```

#### Using IDE (Eclipse, IntelliJ):
1. Create a new Java project
2. Set source folder: `src/`
3. Set output folder: `build/`
4. Add PostgreSQL JDBC JAR to project classpath
5. Run as Java Application: `app.Main`

## Implementation Status

Each Java file contains **TODO** comments indicating what needs to be implemented:

### Phase 1 (Database & Main Window)
- [ ] `config/DBConnection.java` - Database connection
- [ ] `app/Main.java` - Application entry point
- [ ] `ui/MainFrame.java` - Main window with navigation
- [ ] `database/schema.sql` - Create tables

### Phase 2 (Models & Data Access)
- [ ] `model/Student.java`
- [ ] `model/Course.java`
- [ ] `model/Enrollment.java`
- [ ] `dao/StudentDAO.java`
- [ ] `dao/CourseDAO.java`
- [ ] `dao/EnrollmentDAO.java`

### Phase 3 (Services)
- [ ] `service/StudentService.java`
- [ ] `service/TranscriptService.java`
- [ ] `service/ClassListService.java`

### Phase 4 (UI Forms & Panels)
- [ ] `ui/panels/RegisterStudentPanel.java`
- [ ] `ui/panels/EnrollStudentPanel.java`
- [ ] `ui/panels/GradePanel.java`
- [ ] `ui/panels/TranscriptPanel.java`
- [ ] `ui/panels/ClassListPanel.java`

### Phase 5 (Utilities)
- [ ] `ui/components/TableUtils.java`
- [ ] `ui/components/FormUtils.java`
- [ ] `utils/Validator.java`

## Database Schema

### students
- `student_id` (PRIMARY KEY)
- `full_name`
- `full_address`
- `birthdate`
- `place_of_birth`
- `degree`
- `major`

### courses
- `course_id` (PRIMARY KEY)
- `course_name`
- `college`
- `term`
- `descriptive_title`
- `course_year_section`
- `units`
- `days`
- `time`
- `room`
- `school_year`
- `instructor`

### enrollments
- `enrollment_id` (PRIMARY KEY)
- `student_id` (FOREIGN KEY)
- `course_id` (FOREIGN KEY)
- `grade`
- `date_conferred`
- `category`
- `educational_attainment`
- `date_admitted`

## Key SQL Queries

See `database/queries.sql` for all reference queries used by the application:
- Get all students (for dropdowns)
- Get all courses (for dropdowns)
- Get transcript for a student (JOIN query)
- Get class list for a course (JOIN query)
- Get courses for a student

## Implementation Patterns

### Service → DAO Pattern
```java
// In StudentService
public void insertStudent(String fullName, String address, ...) throws Exception {
    Student student = new Student(fullName, address, ...);
    studentDAO.insertStudent(student);
}
```

### Frontend → Service Pattern
```java
// In RegisterStudentPanel
StudentService service = new StudentService();
service.insertStudent(name, address, birthdate, ...);
```

### DAO → Database Pattern
```java
// In StudentDAO
public void insertStudent(Student s) throws Exception {
    Connection conn = DBConnection.getConnection();
    String sql = "INSERT INTO students (...) VALUES (...)";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, s.getFullName());
    // ... set more parameters
    ps.executeUpdate();
    conn.close();
}
```

## Common Issues & Solutions

### Issue: "org.postgresql.Driver class not found"
**Solution**: Ensure PostgreSQL JDBC JAR is in classpath and project build configuration

### Issue: "Connection refused" to database
**Solution**:
- Verify PostgreSQL is running
- Check database URL, username, password in DBConnection.java
- Ensure database exists: `CREATE DATABASE student_system;`

### Issue: "Table does not exist"
**Solution**:
- Execute `database/schema.sql` to create tables
- Verify database connection points to correct database

## Documentation Files

- **STRUCTURE.md** - Detailed project structure and architecture guide
- **System Flow.md** - Data flow for each feature
- **Folder Structure.md** - Directory organization
- **Tickets.md** - Development tickets and tasks
- **README.md** - This file

## Contributing

1. Follow the 3-layer architecture strictly
2. Never bypass the established patterns
3. Add TODO comments for incomplete implementations
4. Keep .gitkeep files to maintain folder structure
5. Update SQL queries in `database/queries.sql` for reference

## License

[Specify license if applicable]

## Author

[Your name/organization]

---

**All files are ready for implementation. Follow the TODO comments in each file and refer to STRUCTURE.md for detailed guidance.**
