# Student Information System — Complete Folder Structure

This file is written for an AI assistant to read and use as a scaffold reference.
Every folder, file, its owner ticket, and its purpose is documented here.
Use this to generate starter code, create files, or answer questions about the project layout.

---

## Project Root

```
student-system/
├── frontend/          ← Java Swing UI (FE team owns this entire folder)
├── backend/           ← JDBC + business logic (BE team owns this entire folder)
├── database/          ← SQL scripts only (BE1 owns this)
├── docs/              ← Documentation (whole team)
├── README.md          ← Project overview and setup guide
└── .gitignore         ← Ignores .class, jars, IDE files
```

---

## frontend/

**Owner:** FE Team (FE1, FE2, FE3)
**Tech:** Java Swing
**Rule:** No SQL. No database connections. Only calls backend Service classes.

```
frontend/
├── src/
│   ├── app/
│   │   └── Main.java
│   │
│   ├── ui/
│   │   ├── MainFrame.java
│   │   │
│   │   ├── panels/
│   │   │   ├── RegisterStudentPanel.java
│   │   │   ├── EnrollStudentPanel.java
│   │   │   ├── GradePanel.java
│   │   │   ├── TranscriptPanel.java
│   │   │   └── ClassListPanel.java
│   │   │
│   │   └── components/
│   │       ├── TableUtils.java
│   │       └── FormUtils.java
│   │
│   └── utils/
│       └── Validator.java
│
└── assets/
    └── icons/
```

### File Details — frontend/

| File | Ticket | Package | Purpose |
|------|--------|---------|---------|
| Main.java | FE1 | app | Entry point. Creates and shows MainFrame. |
| MainFrame.java | FE1 | ui | Main JFrame window. Contains CardLayout. Has sidebar nav with 5 buttons. Switches panels on click. |
| RegisterStudentPanel.java | FE2 | ui.panels | JPanel with form fields: full name, address, birthdate, place of birth, degree, major. Submit calls StudentService.insertStudent(). Clear resets fields. |
| EnrollStudentPanel.java | FE2 | ui.panels | JPanel with JComboBox for student and course selection. Fields: term, school year, category, date admitted. Submit calls EnrollmentDAO.enrollStudent(). |
| GradePanel.java | FE2 | ui.panels | JPanel with JComboBox for student and course. JTextField for grade input (1.0–5.0). Submit calls EnrollmentDAO.addGrade(). |
| TranscriptPanel.java | FE3 | ui.panels | JPanel with JComboBox to select student. Top area shows personal info as JLabels. Bottom area shows JTable: Term, Course Name, Descriptive Title, Final Grade, Units. Data from TranscriptService.getTranscript(). |
| ClassListPanel.java | FE3 | ui.panels | JPanel with JComboBox to select course. Header area shows course info as JLabels. JTable shows: No., Student ID, Student Name, Grade, Units, Course. Footer shows summary counts and signature lines. Data from ClassListService.getClassList(). |
| TableUtils.java | FE3 | ui.components | Static helper methods for creating and formatting JTable instances. Sets column widths, header style, row height. |
| FormUtils.java | FE2 | ui.components | Static helper methods for creating JLabel + JTextField pairs. Keeps form layout consistent across panels. |
| Validator.java | FE2 | utils | Static validation methods. checkNotEmpty(String), isValidDate(String), isValidGrade(double). Returns boolean or throws IllegalArgumentException. |

---

## backend/

**Owner:** BE Team (BE1, BE2, BE3)
**Tech:** Plain Java + JDBC
**Rule:** All SQL goes here. All business logic goes here. Never import Swing classes.

```
backend/
├── src/
│   ├── config/
│   │   └── DBConnection.java
│   │
│   ├── model/
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Enrollment.java
│   │
│   ├── dao/
│   │   ├── StudentDAO.java
│   │   ├── CourseDAO.java
│   │   └── EnrollmentDAO.java
│   │
│   └── service/
│       ├── StudentService.java
│       ├── TranscriptService.java
│       └── ClassListService.java
│
└── lib/
    └── postgresql-42.x.x.jar
```

### File Details — backend/

| File | Ticket | Package | Purpose |
|------|--------|---------|---------|
| DBConnection.java | BE1 | config | Singleton JDBC connection factory. getConnection() returns Connection to school1_db. Credentials stored here. |
| Student.java | BE2 | model | POJO. Fields: studentId, fullName, fullAddress, birthdate, placeOfBirth, degree, major. Getters and setters only. |
| Course.java | BE2 | model | POJO. Fields: courseId, courseName, college, term, descriptiveTitle, courseYearSection, units, days, time, room, schoolYear, instructor. |
| Enrollment.java | BE2 | model | POJO. Fields: enrollmentId, studentId, courseId, grade, dateConferred, category, educationalAttainment, dateAdmitted. |
| StudentDAO.java | BE2 | dao | SQL operations on students table. Methods: insertStudent(Student), getStudentById(int), getAllStudents(). Uses PreparedStatement. |
| CourseDAO.java | BE2 | dao | SQL operations on courses table. Methods: insertCourse(Course), getCourseById(int), getAllCourses(). Uses PreparedStatement. |
| EnrollmentDAO.java | BE2 | dao | SQL operations on enrollments table. Methods: enrollStudent(int, int, String, String, String, Date), addGrade(int, int, double). Uses PreparedStatement. |
| StudentService.java | BE2 | service | Wraps StudentDAO. Called directly by FE2 panels. Methods: insertStudent(...field params...), getAllStudents(). |
| TranscriptService.java | BE3 | service | Executes JOIN query across students + courses + enrollments for one student. Returns TranscriptResult object containing student personal info and List of EnrollmentRecord. |
| ClassListService.java | BE3 | service | Executes JOIN query for all students in one course. Computes: total, passed (grade <= 3.0), failed (grade > 3.0), incomplete (grade is null). Returns ClassListResult. |
| postgresql-42.x.x.jar | BE1 | lib | JDBC driver. Download from https://jdbc.postgresql.org/download/ and place here. Add to classpath in IDE. |

---

## database/

**Owner:** BE1
**Tech:** Plain SQL for PostgreSQL

```
database/
├── schema.sql
├── seed.sql
└── queries.sql
```

### File Details — database/

| File | Ticket | Purpose |
|------|--------|---------|
| schema.sql | BE1 | CREATE DATABASE and all CREATE TABLE statements. Run this first on a fresh PostgreSQL install. Creates: students, courses, enrollments tables. |
| seed.sql | BE1 | INSERT sample data. At least 3 students, 3 courses, 4 enrollment records with grades. Used for testing. |
| queries.sql | BE3 | Collection of reusable SQL queries: TOR JOIN query, class list JOIN query, dropdown queries. Documented with comments. |

---

## docs/

**Owner:** Whole team

```
docs/
├── ERD.png
└── system-flow.md
```

| File | Purpose |
|------|---------|
| ERD.png | Entity relationship diagram showing students, courses, enrollments and their foreign keys. |
| system-flow.md | This file. Describes full architecture, data flow per feature, class responsibilities, method signatures, SQL queries, and team ticket map. |

---

## Root Files

| File | Purpose |
|------|---------|
| README.md | Project overview. Setup instructions. How to run. Tech stack. Team member table. |
| .gitignore | Ignores: *.class, *.jar, backend/lib/, .idea/, *.iml, out/, target/ |

---

## Complete Tree (single block — for AI scaffolding)

```
student-system/
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   └── Main.java
│   │   ├── ui/
│   │   │   ├── MainFrame.java
│   │   │   ├── panels/
│   │   │   │   ├── RegisterStudentPanel.java
│   │   │   │   ├── EnrollStudentPanel.java
│   │   │   │   ├── GradePanel.java
│   │   │   │   ├── TranscriptPanel.java
│   │   │   │   └── ClassListPanel.java
│   │   │   └── components/
│   │   │       ├── TableUtils.java
│   │   │       └── FormUtils.java
│   │   └── utils/
│   │       └── Validator.java
│   └── assets/
│       └── icons/
│
├── backend/
│   ├── src/
│   │   ├── config/
│   │   │   └── DBConnection.java
│   │   ├── model/
│   │   │   ├── Student.java
│   │   │   ├── Course.java
│   │   │   └── Enrollment.java
│   │   ├── dao/
│   │   │   ├── StudentDAO.java
│   │   │   ├── CourseDAO.java
│   │   │   └── EnrollmentDAO.java
│   │   └── service/
│   │       ├── StudentService.java
│   │       ├── TranscriptService.java
│   │       └── ClassListService.java
│   └── lib/
│       └── postgresql-42.x.x.jar
│
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── queries.sql
│
├── docs/
│   ├── ERD.png
│   └── system-flow.md
│
├── README.md
└── .gitignore
```

---

## Package Declarations Reference

Use these exact package names in every Java file.

### Frontend packages
```java
// Main.java
package app;

// MainFrame.java
package ui;

// Panel files
package ui.panels;

// Component files
package ui.components;

// Validator
package utils;
```

### Backend packages
```java
// DBConnection.java
package config;

// Model files
package model;

// DAO files
package dao;

// Service files
package service;
```

---

## Import Reference

### Frontend panels import from backend like this
```java
import service.StudentService;
import service.TranscriptService;
import service.ClassListService;
import dao.EnrollmentDAO;
import dao.CourseDAO;
import model.Student;
import model.Course;
import model.Enrollment;
```

### DAO files import
```java
import config.DBConnection;
import model.Student;   // or Course, Enrollment
import java.sql.*;
import java.util.*;
```

### Service files import
```java
import dao.StudentDAO;
import dao.EnrollmentDAO;
import dao.CourseDAO;
import model.Student;
import java.sql.*;
import java.util.*;
```

---

## .gitignore Content

```
# Compiled Java
*.class
out/
target/
bin/

# JDBC Driver (each dev downloads their own)
backend/lib/
*.jar

# IDE files
.idea/
*.iml
*.iws
*.ipr
.vscode/
*.project
*.classpath

# OS files
.DS_Store
Thumbs.db
```

---

## Notes for AI Generating Code

1. All DAO methods must use `PreparedStatement`, never `Statement` with string concatenation.
2. All DAO methods must have try-catch for `SQLException` and print the stack trace.
3. All frontend Submit button handlers must wrap backend calls in try-catch and show `JOptionPane` on error.
4. `DBConnection.getConnection()` should use `DriverManager.getConnection(url, user, pass)`.
5. Model classes (Student, Course, Enrollment) are plain POJOs — no logic, only fields + getters + setters + constructor.
6. `TranscriptService` and `ClassListService` return custom result objects or Maps — define inner static classes or separate result classes as needed.
7. `MainFrame` uses `CardLayout` — each panel is added with a string key and shown via `cardLayout.show(mainPanel, key)`.
8. Navigation buttons in `MainFrame` should be in a sidebar `JPanel` on the left with `BoxLayout.Y_AXIS`.
9. The JDBC driver class is `org.postgresql.Driver` — load with `Class.forName("org.postgresql.Driver")` if needed.
10. PostgreSQL database name is `school1_db`, default port `5432`, default user `postgres`.