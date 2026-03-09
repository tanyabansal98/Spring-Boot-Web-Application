-- =====================================================
--  Schema Expansion Script
--  Run this on your Oracle ADB (in order)
-- =====================================================

-- ─────────────────────────────────────────────────────
-- 1. Expand Students table with new columns
-- ─────────────────────────────────────────────────────
ALTER TABLE Students ADD (
    Phone         VARCHAR2(20),
    Date_Of_Birth DATE,
    Major         VARCHAR2(100),
    Status        VARCHAR2(20) DEFAULT 'Active'
);

-- ─────────────────────────────────────────────────────
-- 2. Create Courses table
-- ─────────────────────────────────────────────────────
CREATE TABLE Courses (
    Course_Id   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Course_Code VARCHAR2(10)  NOT NULL UNIQUE,
    Course_Name VARCHAR2(200) NOT NULL,
    Credits     NUMBER(2)     DEFAULT 3,
    Instructor  VARCHAR2(100),
    Department  VARCHAR2(100),
    Created_At  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ─────────────────────────────────────────────────────
-- 3. Create Enrollments table (Student <-> Course junction)
-- ─────────────────────────────────────────────────────
CREATE TABLE Enrollments (
    Enrollment_Id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Student_Id    NUMBER NOT NULL REFERENCES Students(Student_Id) ON DELETE CASCADE,
    Course_Id     NUMBER NOT NULL REFERENCES Courses(Course_Id)   ON DELETE CASCADE,
    Grade         VARCHAR2(5),
    Enrolled_At   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_enrollment UNIQUE (Student_Id, Course_Id)
);

-- ─────────────────────────────────────────────────────
-- 4. Seed Courses
-- ─────────────────────────────────────────────────────
INSERT INTO Courses (Course_Code, Course_Name, Credits, Instructor, Department)
VALUES ('CS101', 'Intro to Computer Science', 3, 'Dr. Smith', 'Computer Science');

INSERT INTO Courses (Course_Code, Course_Name, Credits, Instructor, Department)
VALUES ('CS301', 'Database Systems', 3, 'Dr. Wang', 'Computer Science');

INSERT INTO Courses (Course_Code, Course_Name, Credits, Instructor, Department)
VALUES ('MATH201', 'Calculus II', 4, 'Dr. Patel', 'Mathematics');

INSERT INTO Courses (Course_Code, Course_Name, Credits, Instructor, Department)
VALUES ('MATH101', 'Linear Algebra', 3, 'Dr. Lee', 'Mathematics');

INSERT INTO Courses (Course_Code, Course_Name, Credits, Instructor, Department)
VALUES ('CS401', 'Machine Learning', 3, 'Dr. Brown', 'Computer Science');

-- ─────────────────────────────────────────────────────
-- 5. Seed Students with new fields
-- ─────────────────────────────────────────────────────
INSERT INTO Students (Full_Name, Email, Phone, Date_Of_Birth, Major, Status)
VALUES ('Alice Johnson', 'alice@example.com', '617-555-0101',
        DATE '2001-03-15', 'Computer Science', 'Active');

INSERT INTO Students (Full_Name, Email, Phone, Date_Of_Birth, Major, Status)
VALUES ('Bob Martinez', 'bob@example.com', '617-555-0102',
        DATE '2000-07-22', 'Mathematics', 'Active');

INSERT INTO Students (Full_Name, Email, Phone, Date_Of_Birth, Major, Status)
VALUES ('Carol Chen', 'carol@example.com', '617-555-0103',
        DATE '2002-11-05', 'Computer Science', 'Active');

INSERT INTO Students (Full_Name, Email, Phone, Date_Of_Birth, Major, Status)
VALUES ('David Kim', 'david@example.com', '617-555-0104',
        DATE '1999-01-30', 'Mathematics', 'Graduated');

INSERT INTO Students (Full_Name, Email, Phone, Date_Of_Birth, Major, Status)
VALUES ('Eva Patel', 'eva@example.com', '617-555-0105',
        DATE '2001-09-18', 'Computer Science', 'Active');

-- ─────────────────────────────────────────────────────
-- 6. Seed Enrollments
-- ─────────────────────────────────────────────────────
-- Alice → CS101, Database Systems
INSERT INTO Enrollments (Student_Id, Course_Id, Grade)
SELECT s.Student_Id, c.Course_Id, 'A'
FROM Students s, Courses c
WHERE s.Email = 'alice@example.com' AND c.Course_Code = 'CS101';

INSERT INTO Enrollments (Student_Id, Course_Id, Grade)
SELECT s.Student_Id, c.Course_Id, 'B+'
FROM Students s, Courses c
WHERE s.Email = 'alice@example.com' AND c.Course_Code = 'CS301';

-- Bob → MATH201, Linear Algebra
INSERT INTO Enrollments (Student_Id, Course_Id, Grade)
SELECT s.Student_Id, c.Course_Id, 'A-'
FROM Students s, Courses c
WHERE s.Email = 'bob@example.com' AND c.Course_Code = 'MATH201';

INSERT INTO Enrollments (Student_Id, Course_Id, Grade)
SELECT s.Student_Id, c.Course_Id, 'B'
FROM Students s, Courses c
WHERE s.Email = 'bob@example.com' AND c.Course_Code = 'MATH101';

-- Carol → CS101, ML
INSERT INTO Enrollments (Student_Id, Course_Id, Grade)
SELECT s.Student_Id, c.Course_Id, 'A+'
FROM Students s, Courses c
WHERE s.Email = 'carol@example.com' AND c.Course_Code = 'CS101';

INSERT INTO Enrollments (Student_Id, Course_Id, Grade)
SELECT s.Student_Id, c.Course_Id, NULL
FROM Students s, Courses c
WHERE s.Email = 'carol@example.com' AND c.Course_Code = 'CS401';

COMMIT;
