-- ============================================================================
-- P.E.E.R.S. (Physical Exercise Equipment Reservation System)
-- Final MySQL Database Script: Schema Definition & Sample Seed Data
-- Course: Information Management
-- Institution: Ateneo de Davao University
-- Class Code: 4-310 | Group: Group 5 | Milestone: Week 5 Final Submission
-- ============================================================================

DROP DATABASE IF EXISTS peers;
CREATE DATABASE peers;
USE peers;

-- ============================================================================
-- 1. TABLE CREATION (DDL)
-- ============================================================================

-- Table: FACULTY
CREATE TABLE FACULTY (
    FacultyID INT PRIMARY KEY AUTO_INCREMENT,
    FacultyDepartment VARCHAR(100) UNIQUE NOT NULL
);

-- Table: RESERVER (Supertype for Student and Professor)
CREATE TABLE RESERVER (
    ReserverID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    EmailAddress VARCHAR(100) UNIQUE NOT NULL,
    ReserverType ENUM('Student', 'Professor') NOT NULL
);

-- Table: STUDENT (Subtype of RESERVER)
CREATE TABLE STUDENT (
    ReserverID INT PRIMARY KEY,
    StudentID INT UNIQUE NOT NULL,
    FOREIGN KEY (ReserverID) REFERENCES RESERVER(ReserverID) ON DELETE CASCADE
);

-- Table: PROFESSOR (Subtype of RESERVER)
CREATE TABLE PROFESSOR (
    ReserverID INT PRIMARY KEY,
    ProfessorID INT UNIQUE NOT NULL,
    FacultyID INT NOT NULL,
    FOREIGN KEY (ReserverID) REFERENCES RESERVER(ReserverID) ON DELETE CASCADE,
    FOREIGN KEY (FacultyID) REFERENCES FACULTY(FacultyID)
);

-- Table: STAFF
CREATE TABLE STAFF (
    StaffID INT PRIMARY KEY AUTO_INCREMENT,
    StaffName VARCHAR(100) NOT NULL,
    EmailAddress VARCHAR(100) UNIQUE NOT NULL
);

-- Table: EQUIPMENT (Supertype for Sports and Fitness Equipment)
CREATE TABLE EQUIPMENT (
    EquipmentID INT PRIMARY KEY AUTO_INCREMENT,
    EquipmentTypeName VARCHAR(100) UNIQUE NOT NULL,
    TotalQuantity INT NOT NULL,
    AvailableQuantity INT NOT NULL,
    EquipmentType ENUM('Sports', 'Fitness', 'Both') NOT NULL,
    CHECK (AvailableQuantity >= 0 AND AvailableQuantity <= TotalQuantity)
);

-- Table: SPORTS_EQUIPMENT (Subtype of EQUIPMENT)
CREATE TABLE SPORTS_EQUIPMENT (
    EquipmentID INT PRIMARY KEY,
    SportsType VARCHAR(50) NOT NULL,
    FOREIGN KEY (EquipmentID) REFERENCES EQUIPMENT(EquipmentID) ON DELETE CASCADE
);

-- Table: FITNESS_EQUIPMENT (Subtype of EQUIPMENT)
CREATE TABLE FITNESS_EQUIPMENT (
    EquipmentID INT PRIMARY KEY,
    TrainingType ENUM('Strength', 'Cardio', 'Flexibility') NOT NULL,
    FOREIGN KEY (EquipmentID) REFERENCES EQUIPMENT(EquipmentID) ON DELETE CASCADE
);

-- Table: RESERVATION
CREATE TABLE RESERVATION (
    ReservationID INT PRIMARY KEY AUTO_INCREMENT,
    ReserverID INT NOT NULL,
    StaffID INT NULL,
    ReservationDate DATE NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    ReservationStatus ENUM('Pending', 'Approved', 'Denied', 'Cancelled', 'Completed') DEFAULT 'Pending',
    FOREIGN KEY (ReserverID) REFERENCES RESERVER(ReserverID),
    FOREIGN KEY (StaffID) REFERENCES STAFF(StaffID)
);

-- Table: RESERVATION_EQUIPMENT (Bridge / Associative Table)
CREATE TABLE RESERVATION_EQUIPMENT (
    ReservationID INT NOT NULL,
    EquipmentID INT NOT NULL,
    QuantityReserved INT NOT NULL,
    PRIMARY KEY (ReservationID, EquipmentID),
    FOREIGN KEY (ReservationID) REFERENCES RESERVATION(ReservationID) ON DELETE CASCADE,
    FOREIGN KEY (EquipmentID) REFERENCES EQUIPMENT(EquipmentID)
);

-- ============================================================================
-- 2. SAMPLE SEED DATA (DML)
-- ============================================================================

-- ---------- FACULTY ----------
INSERT INTO FACULTY (FacultyID, FacultyDepartment) VALUES
(40000, 'Engineering'),
(40001, 'Arts and Sciences'),
(40002, 'Business and Governance');

-- ---------- RESERVER (Supertype) ----------
INSERT INTO RESERVER (ReserverID, FirstName, LastName, EmailAddress, ReserverType) VALUES
(1, 'Von Najib',  'Sayaman',   'vsayaman@addu.edu.ph',  'Professor'),
(2, 'Juan',       'Dela Cruz', 'jdelacruz@addu.edu.ph', 'Student'),
(3, 'Maria',      'Reyes',     'mreyes@addu.edu.ph',    'Student'),
(4, 'Carlo',      'Bautista',  'cbautista@addu.edu.ph', 'Student'),
(5, 'Angela',     'Torres',    'atorres@addu.edu.ph',   'Student'),
(6, 'Ricardo',    'Santos',    'rsantos@addu.edu.ph',   'Professor');

-- ---------- STUDENT (Subtype) ----------
INSERT INTO STUDENT (ReserverID, StudentID) VALUES
(2, 230001),
(3, 230002),
(4, 230003),
(5, 230004);

-- ---------- PROFESSOR (Subtype) ----------
INSERT INTO PROFESSOR (ReserverID, ProfessorID, FacultyID) VALUES
(1, 30002, 40000),
(6, 30003, 40001);

-- ---------- STAFF ----------
INSERT INTO STAFF (StaffID, StaffName, EmailAddress) VALUES
(10001, 'Equipment Coordinator', 'coord@addu.edu.ph'),
(10002, 'Assistant Coordinator', 'assistant@addu.edu.ph');

-- ---------- EQUIPMENT ----------
INSERT INTO EQUIPMENT (EquipmentID, EquipmentTypeName, TotalQuantity, AvailableQuantity, EquipmentType) VALUES
(701, 'Basketball',          15, 11, 'Sports'),
(702, 'Table Tennis Racket', 15,  8, 'Sports'),
(703, 'Badminton Racket',    15, 13, 'Sports'),
(704, 'Volleyball',          12, 12, 'Sports'),
(705, 'Dumbbell Set',        10, 10, 'Fitness'),
(706, 'Yoga Mat',            15, 15, 'Fitness'),
(707, 'Jump Rope',           20, 20, 'Both'),     -- belongs to both subtypes, never reserved
(708, 'Stopwatch',           10,  0, 'Sports');   -- out of stock (AvailableQuantity = 0)

-- ---------- SPORTS_EQUIPMENT (Subtype) ----------
INSERT INTO SPORTS_EQUIPMENT (EquipmentID, SportsType) VALUES
(701, 'Basketball'),
(702, 'Table Tennis'),
(703, 'Badminton'),
(704, 'Volleyball'),
(707, 'Boxing'),
(708, 'Track');

-- ---------- FITNESS_EQUIPMENT (Subtype) ----------
INSERT INTO FITNESS_EQUIPMENT (EquipmentID, TrainingType) VALUES
(705, 'Strength'),
(706, 'Flexibility'),
(707, 'Cardio');

-- ---------- RESERVATION ----------
INSERT INTO RESERVATION (ReservationID, ReserverID, StaffID, ReservationDate, StartTime, EndTime, ReservationStatus) VALUES
(401, 1, 10001, '2026-08-01', '15:30:00', '17:30:00', 'Approved'),
(402, 2, NULL,  '2026-11-05', '09:00:00', '10:00:00', 'Pending'),
(403, 3, 10001, '2026-10-20', '13:00:00', '14:00:00', 'Completed'),
(404, 4, 10002, '2026-10-22', '10:00:00', '11:00:00', 'Denied'),
(405, 5, 10001, '2026-10-25', '14:00:00', '15:00:00', 'Cancelled'),
(406, 6, 10001, '2026-11-06', '08:00:00', '10:00:00', 'Approved'),
(407, 1, 10001, '2026-11-12', '13:00:00', '15:00:00', 'Approved');

-- ---------- RESERVATION_EQUIPMENT (Line Items) ----------
INSERT INTO RESERVATION_EQUIPMENT (ReservationID, EquipmentID, QuantityReserved) VALUES
(401, 701, 1),
(401, 702, 2),
(401, 703, 1),
(402, 704, 1),
(403, 701, 1),
(404, 702, 1),
(405, 703, 1),
(406, 705, 4),
(406, 706, 10),
(407, 701, 2);
