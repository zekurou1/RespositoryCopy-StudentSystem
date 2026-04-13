-- ============================================================
-- Student Information System - Sample Data
-- PostgreSQL
-- ============================================================

-- Insert sample students
INSERT INTO students (full_name, full_address, birthdate, place_of_birth, degree, major)
VALUES
('John Doe', '123 Main Street, Quezon City', '2002-01-15', 'Manila', 'Bachelor of Science', 'Computer Science'),
('Jane Smith', '456 Elm Street, Makati', '2002-03-22', 'Quezon City', 'Bachelor of Science', 'Information Technology'),
('Bob Johnson', '789 Oak Avenue, Manila', '2001-11-30', 'Pasig', 'Bachelor of Science', 'Computer Science'),
('Alice Williams', '321 Pine Road, Mandaluyong', '2003-05-12', 'Las Pinas', 'Bachelor of Science', 'Information Technology'),
('Charlie Brown', '654 Birch Lane, Taguig', '2002-07-08', 'Makati', 'Bachelor of Science', 'Computer Science');

-- Insert sample courses
INSERT INTO courses (course_name, college, term, descriptive_title, course_year_section, units, days, time, room, school_year, instructor)
VALUES
('CS101', 'College of Engineering and Computer Studies', '1st Semester', 'Introduction to Programming', '1st Year Section A', 3, 'MWF', '09:00-10:00', '101', '2024-2025', 'Dr. Maria Santos'),
('CS102', 'College of Engineering and Computer Studies', '2nd Semester', 'Data Structures', '1st Year Section A', 3, 'TTh', '10:00-11:30', '102', '2024-2025', 'Dr. Juan Dela Cruz'),
('CS201', 'College of Engineering and Computer Studies', '1st Semester', 'Database Systems', '2nd Year Section A', 3, 'MWF', '13:00-14:00', '201', '2024-2025', 'Dr. Rosa Garcia'),
('CS301', 'College of Engineering and Computer Studies', '2nd Semester', 'Software Engineering', '3rd Year Section A', 3, 'TTh', '14:00-15:30', '301', '2024-2025', 'Dr. Antonio Lopez'),
('IT101', 'College of Engineering and Computer Studies', '1st Semester', 'Network Fundamentals', '1st Year Section B', 3, 'MWF', '10:00-11:00', '105', '2024-2025', 'Dr. Patricia Reyes');

-- Insert sample enrollments
INSERT INTO enrollments (student_id, course_id, grade, date_conferred, category, educational_attainment, date_admitted)
VALUES
(1, 1, 1.5, '2024-05-15', 'Regular', 'Undergraduate', '2024-06-01'),
(1, 2, 2.0, '2024-12-15', 'Regular', 'Undergraduate', '2024-06-01'),
(2, 1, 1.25, '2024-05-15', 'Regular', 'Undergraduate', '2024-06-01'),
(2, 5, 2.5, '2024-05-15', 'Regular', 'Undergraduate', '2024-06-01'),
(3, 3, NULL, NULL, 'Regular', 'Undergraduate', '2024-06-01'),
(4, 1, 2.75, '2024-05-15', 'Regular', 'Undergraduate', '2024-06-01'),
(5, 2, 3.0, '2024-12-15', 'Regular', 'Undergraduate', '2024-06-01');
