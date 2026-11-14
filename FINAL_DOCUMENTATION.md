# P.E.E.R.S.: Physical Exercise Equipment Reservation System
## Final Project Documentation
**Course:** Information Management (Class Code: **4-310**)  
**Institution:** Ateneo de Davao University  
**Milestone:** Week 5 – Final Project, Testing & Documentation  
**Date:** November 2026  

---

### GROUP 5 PROJECT TEAM
1. **Dantes, Terenz Jahred C.**
2. **Deza, Micko Dwayne N.**
3. **Fukuoka, Kazuki**
4. **Lambino, Brent Angelo**
5. **Leonardo, Precious Danica**
6. **Miguel, Steven Gabriel G.**
7. **Pulido, Miami Patrice, M.**
8. **Sayaman, Von Najib P.**

---

# Table of Contents
1. Project Title
2. Problem Description
3. Objectives
4. Scope and Limitations
5. Business Rules
6. ER Diagram (Enhanced Entity-Relationship Model)
7. Relational Schema
8. Data Dictionary
9. System Architecture
10. Description of Menu Functions
11. Description of 7–10 Queries
12. Testing
13. Limitations
14. References
15. AI / External Assistance Declaration

---

# 1. Project Title

**P.E.E.R.S.: Physical Exercise Equipment Reservation System**

P.E.E.R.S. is an integrated, relational database management and Java-based console system engineered for university sports complexes, physical education (PE) academic departments, and recreational athletic facilities. Developed specifically for Ateneo de Davao University (AdDU), the system models borrower personas, departmental structures, multi-category sports and fitness equipment, time-slotted reservations, and automated inventory control.

---

# 2. Problem Description

In academic institutions, students and faculty members frequently require specialized physical education and athletic equipment (such as basketballs, volleyballs, table tennis gear, dumbbells, and yoga mats) to fulfill curriculum coursework, conduct athletic training, or stage intramural sports.

Prior to the conception of P.E.E.R.S., equipment distribution was operated entirely through an ad-hoc, paper-based, or physical ID-surrender file-processing approach managed manually by equipment coordinators. When borrowing equipment, students queued at the equipment room window, physically surrendered their school identification card, and verbally requested equipment. The coordinator retained the physical ID until the borrower returned the item.

This traditional approach introduced critical operational failure points:

* **Unaccountable Asset Loss and Damage:** No durable digital audit trail connected a specific borrower to an exact piece of equipment. If an item was damaged, returned incomplete, or lost, identifying the responsible borrower was nearly impossible once their physical ID was returned.
* **No Advance Availability Visibility:** Students had no mechanism to inspect inventory levels beforehand. They frequently endured long waiting queues only to discover that necessary equipment was depleted or already claimed.
* **No Advance Reservation Capability:** Faculty scheduling laboratory drills, PE practical exams, or athletic matches could not guarantee equipment readiness for future class periods.
* **Discrepancies in Inventory Counting:** Total physical stock, current units on loan, and units available for circulation were calculated manually on paper clipboards, leading to frequent inventory shrinkage and discrepancies.
* **Vulnerability of Sensitive Credentials:** Storing physical student identification cards in temporary holding bins exposed students to lost credentials and identity mishandling.

### The Need for P.E.E.R.S.
To eliminate these systemic vulnerabilities, P.E.E.R.S. establishes a normalized, centralized relational database paired with a structured Java Data Access Object (DAO) architecture. The platform enforces strict referential integrity, automated stock calculation ($Available \le Total$), advance booking validation, and comprehensive business reporting.

---

# 3. Objectives

### 3.1 General Objective
To design, normalize, implement, and document a robust, centralized MySQL relational database and an interactive Java application capable of managing athletic equipment inventory, advance reservations, and accountability tracking for Ateneo de Davao University.

### 3.2 Specific Objectives
1. **Centralize Real-Time Inventory Control:** Maintain an accurate catalog of physical equipment categorized into sports, fitness, or dual-purpose categories, enforcing automated inventory consistency checks where $AvailableQuantity \le TotalQuantity$.
2. **Enable Scheduled Advance Reservations:** Permit authenticated students and professors to check equipment availability and reserve equipment for defined calendar dates and time slots.
3. **Establish Borrower Accountability and Audit Trails:** Replace physical ID holding with immutable digital records detailing who reserved each equipment item, which staff coordinator reviewed the request, and the lifecycle status (Pending, Approved, Denied, Cancelled, Completed).
4. **Differentiate User Privileges and Policies:** Model the distinction between individual student borrowers (restricted to single-equipment reservations) and academic professors (authorized to reserve multi-equipment packages for entire classes).
5. **Provide Operational and Analytical Decision Support:** Equip athletic facility managers and department heads with SQL-driven analytical reports (out-of-stock alerts, frequent borrower patterns, equipment demand distributions, and average stock levels).

---

# 4. Scope and Limitations

### 4.1 System Scope
* **User and Personnel Subsystem:** Models supertype `RESERVER` partitioned into `STUDENT` and `PROFESSOR`, linked to university `FACULTY` departments, and administrative `STAFF` coordinators.
* **Equipment Inventory Management:** Complete Create, Read, Update, and Delete (CRUD) operations on equipment records, supporting supertype `EQUIPMENT` and subtypes `SPORTS_EQUIPMENT` and `FITNESS_EQUIPMENT`.
* **Reservation Scheduling Subsystem:** Captures reservation header details (Date, Start Time, End Time, Status) and associates line items via associative table `RESERVATION_EQUIPMENT`.
* **Lifecycle Workflow:** Tracks statuses: `Pending` (initial submission), `Approved` (coordinator authorized), `Denied` (rejected), `Cancelled` (withdrawn by reserver), and `Completed` (equipment safely returned).
* **Operational Query Suite:** Provides eight optimized SQL queries covering filtering (`WHERE`), multi-table inner and outer joins (`JOIN`), aggregation (`COUNT`, `SUM`, `AVG`), grouped criteria (`GROUP BY`, `HAVING`), nested subqueries (`NOT IN`), and ranked sorting (`ORDER BY`, `LIMIT`).

### 4.2 Limitations
* **Console-Based User Interface:** The MVP utilizes a text-based command-line interface (CLI) with defensive scanner parsing rather than a graphical desktop or web front-end.
* **Hardware Independence:** The system does not interface with physical barcode scanners, RFID readers, or automated dispensing lockers; physical verification is conducted by the staff coordinator.
* **Immediate Walk-In Reservations:** Walk-in borrowers are handled seamlessly through the system by initiating an immediate same-day, current-hour reservation rather than maintaining a separate transaction schema.
* **Financial Penalty Automation:** The database records reservation completion but does not compute late fines or process financial payment gateways for overdue items.

---

# 5. Business Rules

The conceptual design and operational integrity of P.E.E.R.S. are governed by sixteen formal business rules:

1. **Reserver Specialization:** Every `RESERVER` must be classified as either a `STUDENT` or a `PROFESSOR`, and cannot be both (Disjoint, Total Specialization).
2. **Departmental Affiliation:** Every `PROFESSOR` belongs to exactly one `FACULTY` department, whereas a `FACULTY` department may encompass one or many `PROFESSOR`s.
3. **Student Active Reservation Cap:** A `STUDENT` reserver can hold only one active reservation (status `Pending` or `Approved`) at any given time.
4. **Historical Submissions:** A `RESERVER` may submit multiple reservations across distinct dates and time slots over their academic lifetime.
5. **Schedule Non-Overlap:** A reserver cannot schedule a reservation whose date and time slot overlaps with an existing active schedule.
6. **Overdue Accountability:** A reserver cannot initiate a new reservation if they have unreturned equipment associated with an expired past schedule.
7. **Single Reserver Ownership:** Each reservation is initiated and owned by exactly one `RESERVER`.
8. **Reservation Equipment Quantities:** A reservation placed by a `STUDENT` must comprise exactly one equipment item, whereas a reservation placed by a `PROFESSOR` may encompass one or multiple equipment items.
9. **Staff Review Assignment:** A reservation is reviewed and authorized by at most one `STAFF` member (initially `NULL` upon submission).
10. **Staff Review Capacity:** A `STAFF` member may review zero, one, or many reservations.
11. **Review Authority:** A `STAFF` coordinator holds authority to mark a reservation as `Approved`, `Denied`, or `Completed`.
12. **Equipment Participation:** An equipment item may appear in zero, one, or multiple reservation line items over time.
13. **Equipment Specialization:** Every `EQUIPMENT` entity must be classified as `SPORTS_EQUIPMENT`, `FITNESS_EQUIPMENT`, or `Both` (Overlapping, Total Specialization).
14. **Sports Equipment Attribute:** Every `SPORTS_EQUIPMENT` must specify a valid `SportsType` (e.g., Basketball, Table Tennis, Badminton, Volleyball, Boxing, Track).
15. **Fitness Equipment Attribute:** Every `FITNESS_EQUIPMENT` must designate a valid `TrainingType` (`Strength`, `Cardio`, or `Flexibility`).
16. **Inventory Boundary Constraint:** For every equipment record, the available quantity must remain non-negative and can never exceed the total quantity:  
    `CHECK (AvailableQuantity >= 0 AND AvailableQuantity <= TotalQuantity)`

---

# 6. ER Diagram (Enhanced Entity-Relationship Model)

### 6.1 Conceptual Architecture Description
The EER model incorporates two critical specialization hierarchies:
1. **RESERVER Supertype (Disjoint `d`, Total Specialization):** Partitioned into `STUDENT` and `PROFESSOR`. A reserver must belong to exactly one subtype.
2. **EQUIPMENT Supertype (Overlapping `o`, Total Specialization):** Partitioned into `SPORTS_EQUIPMENT` and `FITNESS_EQUIPMENT`. An equipment item may belong to sports, fitness, or both (e.g., Jump Rope).

```text
+-----------------------+                    +-----------------------+
|  RESERVER (Supertype) |                    |         STAFF         |
|-----------------------|                    |-----------------------|
| ReserverID (PK)       |                    | StaffID (PK)          |
| FirstName, LastName   |                    | StaffName             |
| EmailAddress (UK)     |                    | EmailAddress (UK)     |
| ReserverType (d)      |                    +-----------------------+
+-----------+-----------+                                |
            | submits (0,N)                              | reviews (0,N)
            v                                            v
+--------------------------------------------------------------------+
|                            RESERVATION                             |
|--------------------------------------------------------------------|
| ReservationID (PK)                                                 |
| ReserverID (FK -> RESERVER)                                        |
| StaffID (FK -> STAFF, Nullable)                                    |
| ReservationDate, StartTime, EndTime                                |
| ReservationStatus                                                  |
+---------------------------------+----------------------------------+
                                  |
                                  | contains (1,N)
                                  v
                  +-------------------------------+
                  |     RESERVATION_EQUIPMENT     |
                  |-------------------------------|
                  | ReservationID (PK, FK)        |
                  | EquipmentID (PK, FK)          |
                  | QuantityReserved              |
                  +---------------+---------------+
                                  ^
                                  | is in (0,N)
                  +---------------+---------------+
                  |      EQUIPMENT (Supertype)    |
                  |-------------------------------|
                  | EquipmentID (PK)              |
                  | EquipmentTypeName (UK)        |
                  | TotalQuantity                 |
                  | AvailableQuantity             |
                  | EquipmentType (o)             |
                  +---------------+---------------+
                                  |
               +------------------+------------------+
               | (overlapping)                       |
               v                                     v
+-------------------------------+     +-------------------------------+
|       SPORTS_EQUIPMENT        |     |       FITNESS_EQUIPMENT       |
|-------------------------------|     |-------------------------------|
| EquipmentID (PK, FK)          |     | EquipmentID (PK, FK)          |
| SportsType                    |     | TrainingType                  |
+-------------------------------+     +-------------------------------+
```

### 6.2 Entity Relationship Cardinality and Participation Matrix

| Entity A | Relationship | Entity B | Cardinality | Participation (A -> B) | Participation (B -> A) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `RESERVER` | Submits | `RESERVATION` | 1 : N | Optional (0,N) | Mandatory (1,1) |
| `STAFF` | Reviews | `RESERVATION` | 1 : N | Optional (0,N) | Optional (0,1) – Nullable before review |
| `FACULTY` | Employs / Has | `PROFESSOR` | 1 : N | Optional (0,N) | Mandatory (1,1) |
| `RESERVATION` | Contains | `RESERVATION_EQUIPMENT` | 1 : N | Mandatory (1,N) | Mandatory (1,1) |
| `EQUIPMENT` | Allocated In | `RESERVATION_EQUIPMENT` | 1 : N | Optional (0,N) | Mandatory (1,1) |
| `RESERVER` | Specializes To | `STUDENT`, `PROFESSOR` | Supertype | Disjoint (d) | Total Specialization (Double line) |
| `EQUIPMENT` | Specializes To | `SPORTS`, `FITNESS` | Supertype | Overlapping (o) | Total Specialization (Double line) |

---

# 7. Relational Schema

The conceptual EER model maps into ten normalized relational schemas in **Third Normal Form (3NF)**:

```text
FACULTY (
    FacultyID [PK],
    FacultyDepartment [UNIQUE]
)

RESERVER (
    ReserverID [PK],
    FirstName,
    LastName,
    EmailAddress [UNIQUE],
    ReserverType
)

STUDENT (
    ReserverID [PK, FK -> RESERVER(ReserverID) ON DELETE CASCADE],
    StudentID [UNIQUE]
)

PROFESSOR (
    ReserverID [PK, FK -> RESERVER(ReserverID) ON DELETE CASCADE],
    ProfessorID [UNIQUE],
    FacultyID [FK -> FACULTY(FacultyID) ON DELETE RESTRICT]
)

STAFF (
    StaffID [PK],
    StaffName,
    EmailAddress [UNIQUE]
)

EQUIPMENT (
    EquipmentID [PK],
    EquipmentTypeName [UNIQUE],
    TotalQuantity,
    AvailableQuantity,
    EquipmentType
)

SPORTS_EQUIPMENT (
    EquipmentID [PK, FK -> EQUIPMENT(EquipmentID) ON DELETE CASCADE],
    SportsType
)

FITNESS_EQUIPMENT (
    EquipmentID [PK, FK -> EQUIPMENT(EquipmentID) ON DELETE CASCADE],
    TrainingType
)

RESERVATION (
    ReservationID [PK],
    ReserverID [FK -> RESERVER(ReserverID) ON DELETE RESTRICT],
    StaffID [FK -> STAFF(StaffID) ON DELETE SET NULL, NULLABLE],
    ReservationDate,
    StartTime,
    EndTime,
    ReservationStatus
)

RESERVATION_EQUIPMENT (
    ReservationID [PK, FK -> RESERVATION(ReservationID) ON DELETE CASCADE],
    EquipmentID [PK, FK -> EQUIPMENT(EquipmentID) ON DELETE RESTRICT],
    QuantityReserved
)
```

### 7.1 Normalization Verification (UNF to 3NF)
* **First Normal Form (1NF):** All multi-valued repeating groups (e.g., multiple equipment items borrowed within a single reservation session) were segregated into the associative bridge relation `RESERVATION_EQUIPMENT`. Every column holds strictly atomic values.
* **Second Normal Form (2NF):** Every non-key attribute is fully functionally dependent on the entire primary key. In `RESERVATION_EQUIPMENT`, the non-key attribute `QuantityReserved` depends on the full composite key `(ReservationID, EquipmentID)`. Item-level descriptors (such as `EquipmentTypeName` and `TotalQuantity`) reside exclusively in `EQUIPMENT`, eliminating partial key dependencies.
* **Third Normal Form (3NF):** No transitive functional dependencies exist ($X \to Y \to Z$). For instance, in `PROFESSOR`, `FacultyDepartment` is not stored directly; instead, `FacultyID` serves as a foreign key to `FACULTY`, ensuring all non-key columns depend solely on the primary key.

---

# 8. Data Dictionary

### 8.1 Table: FACULTY
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `FacultyID` | INT | 4 B | No | **PK** | Surrogate auto-increment primary identifier for academic faculties. |
| `FacultyDepartment` | VARCHAR | 100 | No | UNIQUE | Distinct university faculty division name (e.g., Engineering, Arts & Sciences). |

### 8.2 Table: RESERVER
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `ReserverID` | INT | 4 B | No | **PK** | Unique surrogate identifier for all borrower accounts. |
| `FirstName` | VARCHAR | 50 | No | — | Legal given name of the reserver. |
| `LastName` | VARCHAR | 50 | No | — | Legal surname of the reserver. |
| `EmailAddress` | VARCHAR | 100 | No | UNIQUE | Institutional Ateneo email address used for identity verification. |
| `ReserverType` | ENUM | — | No | — | Discriminator value: `'Student'` or `'Professor'`. |

### 8.3 Table: STUDENT
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `ReserverID` | INT | 4 B | No | **PK, FK** | Inherits ReserverID; cascades on delete of parent reserver. |
| `StudentID` | INT | 4 B | No | UNIQUE | Official university-assigned student ID number (e.g., 230001). |

### 8.4 Table: PROFESSOR
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `ReserverID` | INT | 4 B | No | **PK, FK** | Inherits ReserverID; cascades on delete of parent reserver. |
| `ProfessorID` | INT | 4 B | No | UNIQUE | Official university-assigned faculty/employee payroll ID. |
| `FacultyID` | INT | 4 B | No | **FK** | Links professor to their academic college in `FACULTY`. |

### 8.5 Table: STAFF
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `StaffID` | INT | 4 B | No | **PK** | Unique identifier for sports facility equipment personnel. |
| `StaffName` | VARCHAR | 100 | No | — | Full employee name of the equipment coordinator or assistant. |
| `EmailAddress` | VARCHAR | 100 | No | UNIQUE | Staff institutional email account for administrative communications. |

### 8.6 Table: EQUIPMENT
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `EquipmentID` | INT | 4 B | No | **PK** | Unique surrogate inventory identifier for an equipment model. |
| `EquipmentTypeName` | VARCHAR | 100 | No | UNIQUE | Descriptive item name (e.g., 'Basketball', 'Yoga Mat'). |
| `TotalQuantity` | INT | 4 B | No | — | Total owned inventory count in university possession. |
| `AvailableQuantity` | INT | 4 B | No | — | Units currently in storage and ready for borrowing; validated $\le TotalQuantity$. |
| `EquipmentType` | ENUM | — | No | — | Subtype discriminator: `'Sports'`, `'Fitness'`, or `'Both'`. |

### 8.7 Table: SPORTS_EQUIPMENT
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `EquipmentID` | INT | 4 B | No | **PK, FK** | References `EQUIPMENT(EquipmentID)` with `ON DELETE CASCADE`. |
| `SportsType` | VARCHAR | 50 | No | — | Designated sport (e.g., 'Basketball', 'Table Tennis', 'Badminton', 'Volleyball'). |

### 8.8 Table: FITNESS_EQUIPMENT
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `EquipmentID` | INT | 4 B | No | **PK, FK** | References `EQUIPMENT(EquipmentID)` with `ON DELETE CASCADE`. |
| `TrainingType` | ENUM | — | No | — | Classification of exercise: `'Strength'`, `'Cardio'`, or `'Flexibility'`. |

### 8.9 Table: RESERVATION
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `ReservationID` | INT | 4 B | No | **PK** | Surrogate auto-increment primary identifier for each booking. |
| `ReserverID` | INT | 4 B | No | **FK** | Identifies borrower in `RESERVER`. |
| `StaffID` | INT | 4 B | Yes | **FK** | Assigned equipment coordinator in `STAFF`; `NULL` until processed. |
| `ReservationDate` | DATE | 3 B | No | — | Calendar date of the scheduled equipment borrowing. |
| `StartTime` | TIME | 3 B | No | — | Designated checkout time. |
| `EndTime` | TIME | 3 B | No | — | Mandatory return deadline time. |
| `ReservationStatus` | ENUM | — | No | — | Lifecycle status: `'Pending'`, `'Approved'`, `'Denied'`, `'Cancelled'`, `'Completed'`. |

### 8.10 Table: RESERVATION_EQUIPMENT
| Field Name | Data Type | Size | Null? | Key | Description & Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `ReservationID` | INT | 4 B | No | **PK, FK** | Foreign key to `RESERVATION`; cascades on header deletion. |
| `EquipmentID` | INT | 4 B | No | **PK, FK** | Foreign key to `EQUIPMENT`; restricted from deletion when reserved. |
| `QuantityReserved` | INT | 4 B | No | — | Quantity of this specific equipment item booked for the reservation. |

---

# 9. System Architecture

P.E.E.R.S. follows an industry-standard **3-Tier Layered Architecture** adhering to the **Data Access Object (DAO)** design pattern:

```text
+-------------------------------------------------------------+
|                     1. PRESENTATION TIER                    |
|             Console UI / Interactive CLI Driver             |
|                  Main.java  |  InputHelper.java             |
+------------------------------+------------------------------+
                               | (Method Calls)
                               v
+-------------------------------------------------------------+
|                 2. DATA ACCESS (DAO) TIER                   |
|                   JDBC & Domain Entities                    |
|      EquipmentDAO.java  |  QueryDAO.java  |  DBConnection   |
+------------------------------+------------------------------+
                               | (TCP/IP JDBC: 3306)
                               v
+-------------------------------------------------------------+
|                      3. DATABASE TIER                       |
|                   MySQL 8.x RDBMS Engine                    |
|       peers (Database)  |  Tables, Constraints, Indexes     |
+-------------------------------------------------------------+
```

### Architectural Layer Responsibilities
1. **Presentation Tier (`Main.java` & `InputHelper.java`):** Provides an interactive, crash-resilient console UI. `InputHelper` intercepts user keystrokes, guaranteeing that blank strings, non-numeric inputs, or out-of-bounds menu selections are reprompted before reaching business layers.
2. **Data Access Object Tier (`EquipmentDAO.java`, `QueryDAO.java`, `DBConnection.java`):** Encapsulates all SQL statement compilation and execution using parameterized `PreparedStatement` instances to prevent SQL injection vulnerabilities. Maps `ResultSet` rows into formatted CLI tables and maps database error codes (e.g., MySQL 1451, 1062, 3819) into human-friendly explanations.
3. **Database Tier (MySQL 8.0 Engine):** Stores normalized records across ten tables, executes cost-optimized relational joins, and enforces schema-level constraints including `ON DELETE CASCADE`, `UNIQUE` indexes, and column-level `CHECK` expressions.

---

# 10. Description of Menu Functions

The system navigates via structured menus providing comprehensive CRUD and analytical capabilities:

### 10.1 Main Menu
```text
==========================================
   P.E.E.R.S. - Physical Exercise
   Equipment Reservation System
==========================================
1. Equipment Management (CRUD)
2. Reports & Queries (Week 4)
0. Exit Application
```

| Menu Option | Function Name | Handler Method | Behavioral Description |
| :--- | :--- | :--- | :--- |
| **1** | Equipment Management | `handleEquipmentMenu()` | Navigates into the sub-menu for Equipment inventory CRUD operations. |
| **2** | Reports & Queries | `handleReportsMenu()` | Navigates into the sub-menu for analytical operational queries (Q1–Q8). |
| **0** | Exit Application | `System.exit(0)` | Closes standard input streams, terminates database pools, and exits cleanly. |

### 10.2 Submenu 1: Equipment Management (CRUD Operations)

| Option | Operation | Method | Technical & Business Workflow |
| :--- | :--- | :--- | :--- |
| **1** | **VIEW ALL** | `EquipmentDAO.viewAll()` | Executes `SELECT * FROM EQUIPMENT ORDER BY EquipmentID`. Formats and displays an ASCII table rendering ID, Name, Type, and Available/Total stock. |
| **2** | **SEARCH BY ID** | `EquipmentDAO.searchById(id)` | Prompts for an integer ID, prepares `SELECT * FROM EQUIPMENT WHERE EquipmentID = ?`, and renders the single matched record or reports if not found. |
| **3** | **ADD (INSERT)** | `EquipmentDAO.add(Equipment)` | Prompts for Equipment Name, Type (Sports/Fitness/Both), and Total Quantity. Computes initial `AvailableQuantity = TotalQuantity` and executes parameterized `INSERT INTO EQUIPMENT`. Catches duplicate name conflicts (Error 1062). |
| **4** | **UPDATE** | `EquipmentDAO.update(Equipment)` | Prompts for target ID and updated values. Adjusts `TotalQuantity` and recalculates `AvailableQuantity` using delta arithmetic: `AvailableQuantity = AvailableQuantity + (NewTotal - OldTotal)`. Handles out-of-bounds inventory checks gracefully. |
| **5** | **DELETE** | `EquipmentDAO.delete(id)` | Executes `DELETE FROM EQUIPMENT WHERE EquipmentID = ?`. If the item is actively referenced in existing bookings (`RESERVATION_EQUIPMENT`), intercepts MySQL error 1451 and notifies the user without crashing. |
| **0** | **BACK** | `return` | Returns execution control back to the Main Menu. |

### 10.3 Submenu 2: Reports & Queries (Week 4 Query Suite)

| Option | Query Name | Core SQL Operation | Business Objective |
| :--- | :--- | :--- | :--- |
| **1** | Out-of-Stock Items | `WHERE AvailableQuantity = 0` | Flags depleted gear requiring immediate replenishment. |
| **2** | Scheduled Bookings | `JOIN RESERVER + LEFT JOIN STAFF` | Audits reservations, who requested them, and approving staff. |
| **3** | Reservations by User | `COUNT() + GROUP BY` | Identifies overall borrowing volume per user. |
| **4** | Never Reserved Gear | `WHERE ID NOT IN (Subquery)` | Flags stagnant inventory that has never been booked. |
| **5** | Units by Category | `SUM(Quantity) + GROUP BY` | Calculates borrowing demand across Sports vs Fitness. |
| **6** | Frequent Borrowers | `HAVING COUNT() > 1` | Filters reservers with high-frequency repeat utilization. |
| **7** | Top 5 Equipment | `ORDER BY SUM() DESC LIMIT 5` | Identifies the most popular athletic equipment in the school. |
| **8** | Average Stock Levels | `AVG(TotalQuantity) + GROUP BY` | Compares average inventory depth across equipment categories. |
| **9** | Run All Queries | Sequential execution of 1–8 | Batch-executes all 8 queries for evaluation, grading, and reporting. |
| **0** | Back to Main Menu | `return` | Returns execution control back to the Main Menu. |

---

# 11. Description of 7–10 Queries

P.E.E.R.S. implements eight advanced SQL queries within `QueryDAO.java`. Each query answers a specific business problem using foundational relational algebra operators:

### 11.1 Query 1: Out-of-Stock Equipment (WHERE Clause)
* **Business Question:** Which athletic equipment items currently have zero units available for circulation?
* **Relational Operators:** Selection ($\sigma_{\text{AvailableQuantity} = 0}(\text{EQUIPMENT})$).
* **SQL Statement:**
```sql
SELECT EquipmentID, EquipmentTypeName, EquipmentType, TotalQuantity, AvailableQuantity
FROM EQUIPMENT
WHERE AvailableQuantity = 0
ORDER BY EquipmentID;
```
* **Sample Execution Output:**
```text
+--------------------------------------------------------------------+
| ID   | Equipment Name            | Type       | Total   | Available |
+--------------------------------------------------------------------+
| 708  | Stopwatch                 | Sports     | 10      | 0         |
+--------------------------------------------------------------------+
```
* **Managerial Interpretation:** Alerts equipment coordinators that 'Stopwatch' is completely exhausted and cannot satisfy further student requests until returned or restocked.

---

### 11.2 Query 2: Scheduled Reservations with Reserver and Staff (Multi-Table JOIN)
* **Business Question:** What reservations are currently on record, who placed each request, and which staff member is designated to supervise checkout?
* **Relational Operators:** Theta-join ($\text{RESERVATION} \bowtie \text{RESERVER}$) and Left Outer Join ($\bowtie_{\text{LEFT}} \text{STAFF}$).
* **SQL Statement:**
```sql
SELECT r.ReservationID,
       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName,
       rv.ReserverType,
       r.ReservationDate,
       r.StartTime,
       r.EndTime,
       r.ReservationStatus,
       COALESCE(s.StaffName, 'Unassigned') AS StaffName
FROM RESERVATION r
JOIN RESERVER rv ON r.ReserverID = rv.ReserverID
LEFT JOIN STAFF s ON r.StaffID = s.StaffID
ORDER BY r.ReservationDate DESC;
```
* **Sample Execution Output:**
```text
+-----------------------------------------------------------------------------------------------------+
| ID   | Reserver             | Type       | Date       | Time Slot   | Status     | Reviewing Staff      |
+-----------------------------------------------------------------------------------------------------+
| 407  | Von Najib Sayaman    | Professor  | 2026-11-12 | 13:00-15:00 | Approved   | Equipment Coordinator|
| 406  | Ricardo Santos       | Professor  | 2026-11-06 | 08:00-10:00 | Approved   | Equipment Coordinator|
| 402  | Juan Dela Cruz       | Student    | 2026-11-05 | 09:00-10:00 | Pending    | Unassigned           |
| 405  | Angela Torres        | Student    | 2026-10-25 | 14:00-15:00 | Cancelled  | Equipment Coordinator|
| 404  | Carlo Bautista       | Student    | 2026-10-22 | 10:00-11:00 | Denied     | Assistant Coordinator|
| 403  | Maria Reyes          | Student    | 2026-10-20 | 13:00-14:00 | Completed  | Equipment Coordinator|
| 401  | Von Najib Sayaman    | Professor  | 2026-08-01 | 15:30-17:30 | Approved   | Equipment Coordinator|
+-----------------------------------------------------------------------------------------------------+
```
* **Managerial Interpretation:** Gives counter staff a comprehensive operational agenda. The `COALESCE` function flags pending requests (such as Reservation #402 by Juan Dela Cruz) as 'Unassigned' so coordinators can take immediate review action.

---

### 11.3 Query 3: Total Reservations Submitted by Reserver (COUNT + GROUP BY)
* **Business Question:** How many total reservations has each registered reserver submitted to date?
* **Relational Operators:** Projection, Join, and Grouped Aggregation ($\gamma_{\text{rv.ReserverID, COUNT(ReservationID)}}$).
* **SQL Statement:**
```sql
SELECT rv.ReserverID,
       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName,
       rv.ReserverType,
       COUNT(r.ReservationID) AS TotalReservations
FROM RESERVER rv
JOIN RESERVATION r ON rv.ReserverID = r.ReserverID
GROUP BY rv.ReserverID, rv.FirstName, rv.LastName, rv.ReserverType
ORDER BY TotalReservations DESC;
```
* **Sample Execution Output:**
```text
+------------------------------------------------------------------+
| ID   | Reserver Name             | User Type   | Reservations   |
+------------------------------------------------------------------+
| 1    | Von Najib Sayaman         | Professor   | 2              |
| 2    | Juan Dela Cruz            | Student     | 1              |
| 3    | Maria Reyes               | Student     | 1              |
| 4    | Carlo Bautista            | Student     | 1              |
| 5    | Angela Torres             | Student     | 1              |
| 6    | Ricardo Santos            | Professor   | 1              |
+------------------------------------------------------------------+
```
* **Managerial Interpretation:** Provides facility managers with utilization rankings per user, showing that Professor Sayaman has booked multiple times.

---

### 11.4 Query 4: Equipment Never Reserved (Subquery with NOT IN)
* **Business Question:** Which equipment catalog items have never been included in any reservation record?
* **Relational Operators:** Set Difference / Nested Subquery ($R - \sigma(\dots)$).
* **SQL Statement:**
```sql
SELECT EquipmentID, EquipmentTypeName, EquipmentType, TotalQuantity
FROM EQUIPMENT
WHERE EquipmentID NOT IN (
    SELECT DISTINCT EquipmentID FROM RESERVATION_EQUIPMENT
)
ORDER BY EquipmentID;
```
* **Sample Execution Output:**
```text
+--------------------------------------------------------+
| ID   | Equipment Name            | Type       | Stock |
+--------------------------------------------------------+
| 707  | Jump Rope                 | Both       | 20    |
| 708  | Stopwatch                 | Sports     | 10    |
+--------------------------------------------------------+
```
* **Managerial Interpretation:** Identifies underutilized sports equipment (e.g., Jump Ropes) that may require promotional incentives or re-allocation to general fitness areas.

---

### 11.5 Query 5: Total Units Reserved per Equipment Type (SUM + GROUP BY)
* **Business Question:** What is the cumulative number of units reserved across each equipment classification?
* **Relational Operators:** Join and Grouped Summation ($\gamma_{\text{EquipmentType, SUM(QuantityReserved)}}$).
* **SQL Statement:**
```sql
SELECT e.EquipmentType,
       SUM(re.QuantityReserved) AS TotalUnitsReserved
FROM EQUIPMENT e
JOIN RESERVATION_EQUIPMENT re ON e.EquipmentID = re.EquipmentID
GROUP BY e.EquipmentType
ORDER BY TotalUnitsReserved DESC;
```
* **Sample Execution Output:**
```text
+------------------------------------------+
| Equipment Type  | Total Units Reserved   |
+------------------------------------------+
| Fitness         | 14                     |
| Sports          | 7                      |
+------------------------------------------+
```
* **Managerial Interpretation:** Reveals higher aggregate demand for Fitness equipment (14 units) compared to Sports gear (7 units), informing future procurement budgets.

---

### 11.6 Query 6: Frequent Reservers (GROUP BY + HAVING Clause)
* **Business Question:** Which reservers demonstrate high borrowing activity by submitting more than one reservation?
* **Relational Operators:** Grouped Aggregation with Post-Filtering ($\sigma_{\text{COUNT} > 1}(\gamma(\dots))$).
* **SQL Statement:**
```sql
SELECT rv.ReserverID,
       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName,
       rv.EmailAddress,
       COUNT(r.ReservationID) AS ReservationCount
FROM RESERVER rv
JOIN RESERVATION r ON rv.ReserverID = r.ReserverID
GROUP BY rv.ReserverID, rv.FirstName, rv.LastName, rv.EmailAddress
HAVING COUNT(r.ReservationID) > 1
ORDER BY ReservationCount DESC;
```
* **Sample Execution Output:**
```text
+----------------------------------------------------------------------------+
| ID   | Reserver Name          | Email                        | Reservations|
+----------------------------------------------------------------------------+
| 1    | Von Najib Sayaman      | vsayaman@addu.edu.ph         | 2           |
+----------------------------------------------------------------------------+
```
* **Managerial Interpretation:** Facilitates loyalty programs or faculty-specific block-scheduling by identifying frequent institutional clients.

---

### 11.7 Query 7: Top 5 Most Frequently Reserved Equipment (ORDER BY + LIMIT)
* **Business Question:** What are the top five most demanded equipment items across all reservations?
* **Relational Operators:** Grouped Summation, Sorting, and Top-$K$ Restriction.
* **SQL Statement:**
```sql
SELECT e.EquipmentID,
       e.EquipmentTypeName,
       SUM(re.QuantityReserved) AS TotalQuantityReserved
FROM EQUIPMENT e
JOIN RESERVATION_EQUIPMENT re ON e.EquipmentID = re.EquipmentID
GROUP BY e.EquipmentID, e.EquipmentTypeName
ORDER BY TotalQuantityReserved DESC
LIMIT 5;
```
* **Sample Execution Output:**
```text
+------------------------------------------------------------+
| ID   | Equipment Name            | Total Quantity Borrowed  |
+------------------------------------------------------------+
| 706  | Yoga Mat                  | 10                       |
| 705  | Dumbbell Set              | 4                        |
| 701  | Basketball                | 4                        |
| 702  | Table Tennis Racket       | 3                        |
| 703  | Badminton Racket          | 2                        |
+------------------------------------------------------------+
```
* **Managerial Interpretation:** Highlights 'Yoga Mat' and 'Dumbbell Set' as heavily borrowed items, signaling that storage coordinators should prioritize inspection and sanitation for these items.

---

### 11.8 Query 8: Average Stock Quantity per Equipment Type (AVG + GROUP BY)
* **Business Question:** What is the average inventory depth maintained per equipment category?
* **Relational Operators:** Grouped Arithmetic Mean ($\gamma_{\text{EquipmentType, AVG(TotalQuantity)}}$).
* **SQL Statement:**
```sql
SELECT EquipmentType,
       ROUND(AVG(TotalQuantity), 1) AS AvgTotalStock
FROM EQUIPMENT
GROUP BY EquipmentType
ORDER BY AvgTotalStock DESC;
```
* **Sample Execution Output:**
```text
+--------------------------------------+
| Equipment Type  | Avg Stock Level    |
+--------------------------------------+
| Both            | 20.0               |
| Sports          | 13.0               |
| Fitness         | 12.5               |
+--------------------------------------+
```
* **Managerial Interpretation:** Demonstrates balanced inventory distribution across equipment classifications, with dual-purpose equipment holding the highest average stock level (20.0 units).

---

# 12. Testing

Rigorous functional, boundary, and referential integrity testing was conducted to ensure system stability, input resilience, and compliance with all business rules.

### 12.1 Comprehensive Functional Test Matrix

| Test ID | Feature / Component | Test Description & Input | Expected Output | Actual Output | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-01** | Menu Input Validation | Enter string `"abc"` at Main Menu prompt. | Display *"Invalid input. Please enter a whole number."* and reprompt. | Reprompted cleanly without exception. | **PASS** |
| **TC-02** | Menu Boundary Check | Enter integer `9` at Main Menu (valid range 0–2). | Display *"Please enter a choice between 0 and 2."* and reprompt. | Boundary validated; reprompted. | **PASS** |
| **TC-03** | Equipment: VIEW ALL | Select Option 1 in Equipment Management. | Render all 8 seed equipment records in formatted ASCII table. | 8 records rendered with ID, Name, Type, Available/Total. | **PASS** |
| **TC-04** | Equipment: SEARCH (Found) | Search for existing ID `701`. | Display record for *Basketball (Sports, 11/15)*. | Record retrieved and displayed. | **PASS** |
| **TC-05** | Equipment: SEARCH (Not Found) | Search for non-existent ID `999`. | Display *"No equipment found with ID: 999"*. | Informative not-found message shown. | **PASS** |
| **TC-06** | Equipment: INSERT (Valid) | Add Name: *"Pilates Ball"*, Type: *"Fitness"*, Qty: `10`. | Insert record, Available=10, Total=10, show success message. | Record created successfully; visible in View All. | **PASS** |
| **TC-07** | Equipment: INSERT (Duplicate) | Attempt to add existing Name: *"Basketball"*. | Trap MySQL Error 1062; show duplicate warning without crash. | *"Database Error: An equipment item with this name already exists."* | **PASS** |
| **TC-08** | Equipment: UPDATE | Update ID 701 Total Quantity from 15 to 20. | TotalQuantity=20, AvailableQuantity adjusted from 11 to 16. | Record updated; stock delta applied properly. | **PASS** |
| **TC-09** | Equipment: DELETE (Referenced) | Attempt to delete ID `701` (reserved in reservation 401). | Trap MySQL Error 1451; prevent deletion, show foreign key warning. | *"Cannot delete or modify: equipment is currently referenced in reservations."* | **PASS** |
| **TC-10** | Equipment: DELETE (Unreferenced) | Delete newly created item *"Pilates Ball"*. | Row removed from database; display *"Equipment deleted successfully."* | Row deleted successfully. | **PASS** |
| **TC-11** | Query 1: WHERE | Execute Out-of-Stock Equipment query. | Return exactly 1 item: `Stopwatch (Available = 0)`. | Item 708 returned accurately. | **PASS** |
| **TC-12** | Query 2: JOIN | Execute Scheduled Reservations query. | Return 7 reservation records joined with Reserver and Staff names. | 7 joined rows rendered with COALESCE fallback for unassigned staff. | **PASS** |
| **TC-13** | Query 4: Subquery | Execute Never Reserved Equipment query. | Return items not in `RESERVATION_EQUIPMENT` (707, 708). | Items 707 (Jump Rope) and 708 (Stopwatch) returned. | **PASS** |
| **TC-14** | Query 6: HAVING | Execute Frequent Reservers query (HAVING > 1). | Return only reservers with > 1 booking (Reserver 1). | Von Najib Sayaman returned with count 2. | **PASS** |
| **TC-15** | Query 9: Batch Execution | Execute option 9 (Run All Queries). | Sequential execution of Q1 through Q8 with final completion notice. | All 8 queries printed consecutively without error. | **PASS** |

---

# 13. Limitations

While P.E.E.R.S. satisfies all core relational and inventory requirements, certain technical and scope boundaries are acknowledged:

1. **Text-Based Interface:** The system operates as a command-line application. While fast and highly portable, it lacks graphical representations (such as interactive calendar pickers or image previews of equipment).
2. **Manual Verification at Distribution:** Physical verification relies on staff coordinators inspecting physical equipment condition during checkout and return, rather than barcode or RFID scanning hardware.
3. **Reservation Modifications:** In the current schema, modifying the date or time of an approved reservation requires cancelling the initial record and submitting a revised reservation.
4. **Multi-Equipment Student Restrictions:** In accordance with university policy, student reservations are constrained to a single equipment item to prevent hoarding, whereas professors may reserve bulk quantities.

### 13.1 Future Enhancements
* **Full-Stack Web Interface:** Developing a responsive web dashboard (Spring Boot + React) for mobile self-service reservations.
* **Automated Notifications:** Integrating SMTP email and SMS alerts for approval confirmations and return deadline reminders.
* **Barcode & QR Tagging:** Generating dynamic QR codes for each reservation to enable one-second checkouts via mobile camera scanning.

---

# 14. References

* Elmasri, R., & Navathe, S. B. (2015). *Fundamentals of Database Systems* (7th ed.). Pearson.
* Silberschatz, A., Korth, H. F., & Sudarshan, S. (2020). *Database System Concepts* (7th ed.). McGraw-Hill.
* Oracle Corporation. (2024). *MySQL 8.0 Reference Manual: Constraints, Foreign Keys, and Storage Engines*. Oracle Documentation.
* Oracle Corporation. (2024). *Java Platform, Standard Edition 21 API Specification: java.sql Package*. Oracle Documentation.
* Bloch, J. (2018). *Effective Java* (3rd ed.). Addison-Wesley Professional.

---

# 15. AI / External Assistance Declaration

### Transparency and Integrity Declaration
In accordance with Ateneo de Davao University academic integrity policies, the project team declares the following external tools and assistance utilized in the realization of P.E.E.R.S.:

* **AI Assistance:** Generative AI tools (including Claude, ChatGPT, and Antigravity) were utilized as coding assistants for syntax verification, boilerplate formatting of Data Access Objects (DAOs), proofreading documentation against rubric requirements, and drafting CSS styles for the final PDF presentation.
* **Database Design & Normalization:** All entity modeling, normalization (1NF to 3NF), and business rules were formulated and validated by the student members of Group 5 based on university athletic department interviews.
* **Code Verification:** All Java source code, SQL queries, and test executions were compiled, executed, and validated locally on Java 21 LTS and MySQL 8.0 by the group members.
