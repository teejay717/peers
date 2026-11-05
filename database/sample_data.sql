-- ============================================================
-- P.E.E.R.S sample data
-- Run schema.sql FIRST, then this file.
-- Safe to re-run: it clears the tables before inserting.
-- ============================================================
USE peers;

-- ---------- Reset (children first, then parents) ----------
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM RESERVATION_EQUIPMENT;
DELETE FROM RESERVATION;
DELETE FROM SPORTS_EQUIPMENT;
DELETE FROM FITNESS_EQUIPMENT;
DELETE FROM EQUIPMENT;
DELETE FROM STAFF;
DELETE FROM PROFESSOR;
DELETE FROM STUDENT;
DELETE FROM RESERVER;
DELETE FROM FACULTY;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------- FACULTY ----------
INSERT INTO FACULTY (FacultyID, FacultyDepartment) VALUES
(40000, 'Engineering'),
(40001, 'Arts and Sciences'),
(40002, 'Business and Governance');

-- ---------- RESERVER (supertype) ----------
INSERT INTO RESERVER (ReserverID, FirstName, LastName, EmailAddress, ReserverType) VALUES
(1, 'Von Najib',  'Sayaman',   'vsayaman@addu.edu.ph',  'Professor'),
(2, 'Juan',       'Dela Cruz', 'jdelacruz@addu.edu.ph', 'Student'),
(3, 'Maria',      'Reyes',     'mreyes@addu.edu.ph',    'Student'),
(4, 'Carlo',      'Bautista',  'cbautista@addu.edu.ph', 'Student'),
(5, 'Angela',     'Torres',    'atorres@addu.edu.ph',   'Student'),
(6, 'Ricardo',    'Santos',    'rsantos@addu.edu.ph',   'Professor');

-- ---------- STUDENT (subtype) ----------
INSERT INTO STUDENT (ReserverID, StudentID) VALUES
(2, 230001),
(3, 230002),
(4, 230003),
(5, 230004);

-- ---------- PROFESSOR (subtype) ----------
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
(708, 'Stopwatch',           10,  0, 'Sports');   -- out of stock (AvailableQuantity = 0 for Q1)

-- ---------- SPORTS_EQUIPMENT (subtype) ----------
INSERT INTO SPORTS_EQUIPMENT (EquipmentID, SportsType) VALUES
(701, 'Basketball'),
(702, 'Table Tennis'),
(703, 'Badminton'),
(704, 'Volleyball'),
(707, 'Boxing'),
(708, 'Track');

-- ---------- FITNESS_EQUIPMENT (subtype) ----------
INSERT INTO FITNESS_EQUIPMENT (EquipmentID, TrainingType) VALUES
(705, 'Strength'),
(706, 'Flexibility'),
(707, 'Cardio');

-- ---------- RESERVATION ----------
-- 401: professor booking from normalization example
-- 402: pending student request (no staff assigned yet)
-- 403: completed (equipment returned)
-- 404: denied
-- 405: cancelled by student
-- 406: approved professor booking
-- 407: second approved reservation for Reserver 1 (for Q6 HAVING > 1)
INSERT INTO RESERVATION (ReservationID, ReserverID, StaffID, ReservationDate, StartTime, EndTime, ReservationStatus) VALUES
(401, 1, 10001, '2026-08-01', '15:30:00', '17:30:00', 'Approved'),
(402, 2, NULL,  '2026-11-05', '09:00:00', '10:00:00', 'Pending'),
(403, 3, 10001, '2026-10-20', '13:00:00', '14:00:00', 'Completed'),
(404, 4, 10002, '2026-10-22', '10:00:00', '11:00:00', 'Denied'),
(405, 5, 10001, '2026-10-25', '14:00:00', '15:00:00', 'Cancelled'),
(406, 6, 10001, '2026-11-06', '08:00:00', '10:00:00', 'Approved'),
(407, 1, 10001, '2026-11-12', '13:00:00', '15:00:00', 'Approved');

-- ---------- RESERVATION_EQUIPMENT (line items) ----------
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
