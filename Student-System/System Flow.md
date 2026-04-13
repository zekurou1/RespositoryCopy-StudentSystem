# Student Information System — System Flow

## Project Overview

A Java-based Student Information System with:
- Frontend: Java Swing (desktop GUI)
- Backend: Plain Java with JDBC
- Database: PostgreSQL
- Communication: Direct Java class import (no REST API)

The system generates two official documents:
1. Transcript of Records (TOR)
2. Class List with summary

---

## Architecture — 3 Layers

```
[Java Swing UI]  →  [Service Layer]  →  [DAO Layer]  →  [PostgreSQL]
   frontend            backend             backend          database
```

### Rules (strictly enforced)
- Frontend NEVER writes SQL
- Frontend NEVER connects to the database directly
- Frontend ONLY calls Service methods
- DAO handles ALL SQL queries
- Service handles ALL business logic and calculations

---

## Database Schema

### Table: students
```sql
CREATE TABLE students (
  student_id        SERIAL PRIMARY KEY,
  full_name         VARCHAR(100),
  full_address      VARCHAR(200),
  birthdate         DATE,
  place_of_birth    VARCHAR(100),
  degree            VARCHAR(50),
  major             VARCHAR(50)
);
```

### Table: courses
```sql
CREATE TABLE courses (
  course_id           SERIAL PRIMARY KEY,
  course_name         VARCHAR(100),
  college             VARCHAR(100),
  term                VARCHAR(20),
  descriptive_title   VARCHAR(200),
  course_year_section VARCHAR(50),
  units               INTEGER,
  days                VARCHAR(20),
  time                VARCHAR(20),
  room                VARCHAR(20),
  school_year         VARCHAR(20),
  instructor          VARCHAR(100)
);
```

### Table: enrollments
```sql
CREATE TABLE enrollments (
  enrollment_id          SERIAL PRIMARY KEY,
  student_id             INTEGER REFERENCES students(student_id),
  course_id              INTEGER REFERENCES courses(course_id),
  grade                  NUMERIC(3,2),
  date_conferred         DATE,
  category               VARCHAR(50),
  educational_attainment VARCHAR(50),
  date_admitted          DATE
);
```

### Relationships
- One student → many enrollments
- One course → many enrollments
- One enrollment → one student + one course

---

## Data Flow — Feature by Feature

### 1. Register Student

```
UI: RegisterStudentPanel
  → User fills form (name, address, birthdate, place of birth, degree, major)
  → Clicks Submit
  → Validator.validate() checks empty fields, date format
  → StudentService.insertStudent(Student)
  → StudentDAO.insertStudent(Student)
  → SQL: INSERT INTO students (...) VALUES (?)
  → Returns: success or error message
  → UI shows dialog: "Student Registered Successfully"
```

### 2. Enroll Student to Course

```
UI: EnrollStudentPanel
  → Dropdown loads all students via StudentService.getAllStudents()
  → Dropdown loads all courses via CourseDAO.getAllCourses()
  → User selects student + course + term + school year + category + date admitted
  → Clicks Submit
  → EnrollmentDAO.enrollStudent(studentId, courseId, ...)
  → SQL: INSERT INTO enrollments (...) VALUES (?)
  → UI shows dialog: "Student Enrolled Successfully"
```

### 3. Encode Grade

```
UI: GradePanel
  → Dropdown loads all students
  → Dropdown loads courses for selected student
  → User enters grade (1.0 to 5.0)
  → Validator checks grade range
  → EnrollmentDAO.addGrade(studentId, courseId, grade)
  → SQL: UPDATE enrollments SET grade = ? WHERE student_id = ? AND course_id = ?
  → UI shows dialog: "Grade Saved"
```

### 4. View Transcript of Records (TOR)

```
UI: TranscriptPanel
  → Dropdown loads all students
  → User selects student → clicks View
  → TranscriptService.getTranscript(studentId)
  → SQL JOIN query:
      SELECT s.full_name, s.full_address, s.birthdate,
             s.place_of_birth, s.degree, s.major,
             e.date_conferred, e.category,
             e.educational_attainment, e.date_admitted,
             c.term, c.course_name, c.descriptive_title,
             e.grade, c.units, c.school_year
      FROM enrollments e
      JOIN students s ON e.student_id = s.student_id
      JOIN courses  c ON e.course_id  = c.course_id
      WHERE s.student_id = ?
  → Returns: student personal info + List<EnrollmentRecord>
  → UI renders:
      Top section: JLabel fields (name, address, birthdate, etc.)
      Table section: JTable with columns:
        Term | Course Name | Descriptive Title | Final Grade | Units
```

### 5. View Class List

```
UI: ClassListPanel
  → Dropdown loads all courses
  → User selects course → clicks View
  → ClassListService.getClassList(courseId)
  → SQL JOIN query:
      SELECT s.student_id, s.full_name, e.grade, c.units, s.degree
      FROM enrollments e
      JOIN students s ON e.student_id = s.student_id
      JOIN courses  c ON e.course_id  = c.course_id
      WHERE e.course_id = ?
      ORDER BY s.full_name ASC
  → ClassListService computes summary:
      total      = list.size()
      passed     = count where grade != null && grade <= 3.0
      failed     = count where grade != null && grade >  3.0
      incomplete = count where grade == null
  → UI renders:
      Top section: JLabel fields (institution, college, subject code, etc.)
      Table section: JTable with columns:
        No. | Student ID | Student Name | Grade | Units | Course
      Bottom section: summary counts + signature lines
```

---

## Class Responsibilities

### Frontend

| Class | Package | Responsibility |
|-------|---------|----------------|
| Main.java | app | Launches the application, creates MainFrame |
| MainFrame.java | ui | Main JFrame, CardLayout panel switcher, navigation sidebar |
| RegisterStudentPanel.java | ui.panels | Form UI for registering a new student |
| EnrollStudentPanel.java | ui.panels | Form UI for enrolling student into a course |
| GradePanel.java | ui.panels | Form UI for encoding and updating grades |
| TranscriptPanel.java | ui.panels | Displays TOR: personal info labels + JTable |
| ClassListPanel.java | ui.panels | Displays class list JTable + summary section |
| TableUtils.java | ui.components | Reusable JTable setup, column sizing, formatting |
| FormUtils.java | ui.components | Reusable JLabel + JTextField factory methods |
| Validator.java | utils | Input validation: empty fields, date format, grade range |

### Backend

| Class | Package | Responsibility |
|-------|---------|----------------|
| DBConnection.java | config | Returns a live JDBC Connection to PostgreSQL |
| Student.java | model | POJO: fields matching students table |
| Course.java | model | POJO: fields matching courses table |
| Enrollment.java | model | POJO: fields matching enrollments table |
| StudentDAO.java | dao | SQL: insertStudent, getAllStudents, getStudentById |
| CourseDAO.java | dao | SQL: insertCourse, getAllCourses, getCourseById |
| EnrollmentDAO.java | dao | SQL: enrollStudent, addGrade |
| StudentService.java | service | Wraps StudentDAO, called by frontend forms |
| TranscriptService.java | service | JOIN query for TOR, returns student info + record list |
| ClassListService.java | service | JOIN query for class list, computes summary counts |

---

## Method Signatures

### DBConnection.java
```java
public static Connection getConnection() throws SQLException
```

### StudentDAO.java
```java
public void insertStudent(Student s) throws SQLException
public Student getStudentById(int id) throws SQLException
public List<Student> getAllStudents() throws SQLException
```

### CourseDAO.java
```java
public void insertCourse(Course c) throws SQLException
public Course getCourseById(int id) throws SQLException
public List<Course> getAllCourses() throws SQLException
```

### EnrollmentDAO.java
```java
public void enrollStudent(int studentId, int courseId, String term,
                          String schoolYear, String category,
                          Date dateAdmitted) throws SQLException
public void addGrade(int studentId, int courseId, double grade) throws SQLException
```

### StudentService.java
```java
public void insertStudent(String fullName, String address, Date birthdate,
                          String placeOfBirth, String degree,
                          String major) throws SQLException
public List<Student> getAllStudents() throws SQLException
```

### TranscriptService.java
```java
public TranscriptResult getTranscript(int studentId) throws SQLException
// Returns: student personal fields + List<EnrollmentRecord>
```

### ClassListService.java
```java
public ClassListResult getClassList(int courseId) throws SQLException
// Returns: course info + List<StudentRecord> + summary counts
```

---

## UI Navigation Flow

```
App Launch → Main.java
  → creates MainFrame
    → MainFrame has CardLayout with 5 panels:

    [Sidebar Navigation]
    ┌──────────────────────────────────────────┐
    │  [Register Student]  → RegisterStudentPanel  │
    │  [Enroll Student]    → EnrollStudentPanel    │
    │  [Encode Grades]     → GradePanel            │
    │  [View TOR]          → TranscriptPanel       │
    │  [View Class List]   → ClassListPanel        │
    └──────────────────────────────────────────┘

Clicking a button → cardLayout.show(mainPanel, panelName)
```

---

## Document Output Formats

### Document 1: Transcript of Records (TOR)

```
Personal Information (JLabels):
  Name:                [full_name]
  Address:             [full_address]
  Date of Birth:       [birthdate]
  Place of Birth:      [place_of_birth]
  Degree/Course:       [degree]
  Major:               [major]
  Date Conferred:      [date_conferred]
  Category:            [category]
  Educational Attain.: [educational_attainment]
  Date Admitted:       [date_admitted]

Course Table (JTable):
  Term | Course Name | Descriptive Title | Final Grade | Units
```

### Document 2: Class List

```
Header (JLabels):
  Institution:    Technological University of the Philippines
  College:        [college]
  Subject Code:   [course_name]
  Descriptive:    [descriptive_title]
  Units:          [units]
  Course/Yr/Sec:  [course_year_section]
  Term/SY:        [term], [school_year]
  Days:           [days]
  Schedule:       [time]
  Room:           [room]

Student Table (JTable):
  No. | Student ID | Student Name | Grade | Units | Course

Footer:
  xxxxxxxxxx Nothing Follows xxxxxxxxxx

Summary:
  No. of Students Enrolled:    [total]
  No. of Students Dropped:     [dropped]
  No. of Students Passed:      [passed]     (grade <= 3.0)
  No. of Students Failed:      [failed]     (grade > 3.0)
  No. of Students Incomplete:  [incomplete] (grade is null)

Signature Lines:
  Instructor/Professor _________ Date _________
  Department Head      _________ Date _________
  Dean of College      _________ Date _________
```

---

## Key SQL Queries

### TOR Query
```sql
SELECT
  s.full_name, s.full_address, s.birthdate,
  s.place_of_birth, s.degree, s.major,
  e.date_conferred, e.category,
  e.educational_attainment, e.date_admitted,
  c.term, c.course_name, c.descriptive_title,
  e.grade, c.units, c.school_year
FROM enrollments e
JOIN students s ON e.student_id = s.student_id
JOIN courses  c ON e.course_id  = c.course_id
WHERE s.student_id = ?;
```

### Class List Query
```sql
SELECT
  s.student_id, s.full_name,
  e.grade, c.units, s.degree
FROM enrollments e
JOIN students s ON e.student_id = s.student_id
JOIN courses  c ON e.course_id  = c.course_id
WHERE e.course_id = ?
ORDER BY s.full_name ASC;
```

### Get All Students (for dropdowns)
```sql
SELECT student_id, full_name FROM students ORDER BY full_name ASC;
```

### Get All Courses (for dropdowns)
```sql
SELECT course_id, course_name, descriptive_title FROM courses ORDER BY course_name ASC;
```

---

## Team Ticket Summary

| Ticket | Files Owned | Depends On |
|--------|-------------|------------|
| BE1 | DBConnection.java, schema.sql, seed.sql, queries.sql, postgresql jar | nothing |
| BE2 | Student/Course/Enrollment models, all DAO classes, StudentService.java | BE1 |
| BE3 | TranscriptService.java, ClassListService.java | BE1, BE2 |
| FE1 | Main.java, MainFrame.java | nothing |
| FE2 | RegisterStudentPanel, EnrollStudentPanel, GradePanel, FormUtils, Validator | FE1, BE2 |
| FE3 | TranscriptPanel, ClassListPanel, TableUtils | FE1, BE3 |

### Build Order
```
Phase 1 (parallel): BE1 + FE1
Phase 2:            BE2
Phase 3 (parallel): BE3 + FE2
Phase 4:            FE3
```