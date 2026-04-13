-- ============================================================
-- Student Information System - Key Queries
-- PostgreSQL
-- ============================================================
-- These are the main SQL queries used by the backend

-- ============================================================
-- 1. Get All Students (for dropdowns)
-- ============================================================
-- Used by: EnrollStudentPanel, GradePanel, TranscriptPanel
SELECT student_id, full_name
FROM students
ORDER BY full_name ASC;

-- ============================================================
-- 2. Get All Courses (for dropdowns)
-- ============================================================
-- Used by: EnrollStudentPanel, ClassListPanel
SELECT course_id, course_name, descriptive_title
FROM courses
ORDER BY course_name ASC;

-- ============================================================
-- 3. Get Courses for a Student (for GradePanel)
-- ============================================================
-- Used by: GradePanel - loads courses when student is selected
SELECT DISTINCT c.course_id, c.course_name, c.descriptive_title
FROM courses c
JOIN enrollments e ON c.course_id = e.course_id
WHERE e.student_id = ?
ORDER BY c.course_name ASC;

-- ============================================================
-- 4. Get Transcript of Records (TOR) for a Student
-- ============================================================
-- Used by: TranscriptService.getTranscript()
-- Returns: Student personal info + enrollment records
SELECT
  s.student_id, s.full_name, s.full_address, s.birthdate,
  s.place_of_birth, s.degree, s.major,
  e.date_conferred, e.category,
  e.educational_attainment, e.date_admitted,
  c.term, c.course_name, c.descriptive_title,
  e.grade, c.units, c.school_year
FROM enrollments e
JOIN students s ON e.student_id = s.student_id
JOIN courses c ON e.course_id = c.course_id
WHERE s.student_id = ?
ORDER BY c.school_year, c.term;

-- ============================================================
-- 5. Get Class List for a Course
-- ============================================================
-- Used by: ClassListService.getClassList()
-- Returns: Course info + student enrollment records
SELECT
  s.student_id, s.full_name, e.grade, c.units, s.degree,
  c.course_id, c.course_name, c.descriptive_title, c.college,
  c.course_year_section, c.term, c.school_year, c.days, c.time,
  c.room, c.units, c.instructor
FROM enrollments e
JOIN students s ON e.student_id = s.student_id
JOIN courses c ON e.course_id = c.course_id
WHERE e.course_id = ?
ORDER BY s.full_name ASC;

-- ============================================================
-- Additional Utility Queries
-- ============================================================

-- Get a student by ID
SELECT * FROM students WHERE student_id = ?;

-- Get a course by ID
SELECT * FROM courses WHERE course_id = ?;

-- Get enrollment details for a student
SELECT e.*, c.course_name, c.descriptive_title, c.term
FROM enrollments e
JOIN courses c ON e.course_id = c.course_id
WHERE e.student_id = ?;

-- Get all students in a course (simple list)
SELECT s.student_id, s.full_name FROM students s
JOIN enrollments e ON s.student_id = e.student_id
WHERE e.course_id = ?
ORDER BY s.full_name ASC;
