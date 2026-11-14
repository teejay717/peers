# P.E.E.R.S. Final Presentation and Demonstration Guide
**Course:** Information Management (Class Code: **4-310**)  
**Group:** **Group 5**  
**Project:** Physical Exercise Equipment Reservation System (P.E.E.R.S.)  
**Time Limit:** 10–15 Minutes (All 8 Members Must Present)

---

## 1. Presentation Sequence & Member Speaking Roles (15-Minute Target)

| Segment | Time | Topic | Speaker | Key Points to Cover |
| :--- | :--- | :--- | :--- | :--- |
| **Part 1** | 0:00 – 1:00 (1 min) | **Problem Statement & System Overview** | **Speaker 1:** *Dantes, Terenz Jahred C.* | Introduce P.E.E.R.S.; explain the flaws of the current manual ID-surrender approach (loss, no accountability, long queues) and state our core objectives. |
| **Part 2** | 1:00 – 3:00 (2 mins) | **ERD, Specialization & Database Design** | **Speaker 2:** *Deza, Micko Dwayne N.* | Explain EER design: `RESERVER` supertype (Disjoint `STUDENT` vs `PROFESSOR`) and `EQUIPMENT` supertype (Overlapping `SPORTS` vs `FITNESS`). Point out cardinalities. |
| **Part 3** | 3:00 – 5:00 (2 mins) | **Relational Schema & Normalization (3NF)** | **Speaker 3:** *Fukuoka, Kazuki* | Explain the 10 relational tables, 3NF compliance (elimination of repeating groups in `RESERVATION_EQUIPMENT` and partial/transitive dependencies), and integrity constraints. |
| **Part 4** | 5:00 – 6:00 (1 min) | **System Architecture & Tech Stack** | **Speaker 4:** *Lambino, Brent Angelo* | Introduce the 3-Tier Layered Architecture: CLI Menu (`Main`, `InputHelper`) $\rightarrow$ DAO Layer (`EquipmentDAO`, `QueryDAO`, `DBConnection`) $\rightarrow$ MySQL 8.0 Engine. |
| **Part 5** | 6:00 – 8:00 (2 mins) | **Live Demo: VIEW & INSERT** | **Speaker 5:** *Leonardo, Precious Danica* | Live demo on console: View All Equipment (Option 1) $\rightarrow$ Search by ID (Option 2) $\rightarrow$ Insert new equipment record (Option 3). |
| **Part 6** | 8:00 – 10:00 (2 mins) | **Live Demo: UPDATE, DELETE & EXIT** | **Speaker 6:** *Miguel, Steven Gabriel G.* | Live demo: Update stock/details (Option 4) $\rightarrow$ Attempt to delete referenced item (show Error 1451 trap) $\rightarrow$ Delete unreferenced item (Option 5). |
| **Part 7** | 10:00 – 13:00 (3 mins) | **Live Demo: 2–3 Complex SQL Queries** | **Speaker 7:** *Pulido, Miami Patrice, M.* | Run Query 2 (Multi-table `JOIN` with `COALESCE`), Query 4 (`NOT IN` Subquery), and Query 6 (`GROUP BY` + `HAVING > 1`). Explain managerial value. |
| **Part 8** | 13:00 – 15:00 (2 mins) | **Defense Q&A & Wrap-Up** | **Speaker 8:** *Sayaman, Von Najib P.* | Conclude presentation, coordinate panelist questions, and invite faculty inquiry. |

---

## 2. Step-by-Step Live Demonstration Script

Before starting, ensure MySQL is running, the database is fresh (`mysql -u root -p < database.sql`), and launch the app in VS Code (`F5`) or via command line:
```powershell
java -cp "target/classes;lib/mysql-connector-j-8.4.0.jar" edu.addu.peers.Main
```

### Demonstration Steps (Follow this exact order):

#### Step 1: Navigate to Equipment Management
- At Main Menu prompt: `Enter your choice (0-2):`
- **Type:** `1` and press Enter.

#### Step 2: Demonstrate VIEW (Option 1 & 2)
- At Equipment Menu: `Enter your choice (0-5):`
- **Type:** `1` (View All)
  - *Talking point:* "Here the system displays all 8 equipment records with clean formatting showing their current available vs total inventory."
- **Type:** `2` (Search by ID)
  - Enter Equipment ID: `701`
  - *Talking point:* "Searching for ID 701 returns Basketball (11/15 available)."
  - Enter Equipment ID: `999`
  - *Talking point:* "Searching for a non-existent ID displays an informative message without crashing."

#### Step 3: Demonstrate INSERT (Option 3)
- **Type:** `3` (Add New Equipment)
  - Enter Equipment Name: `Pilates Ball`
  - Enter Equipment Type: `Fitness`
  - Enter Total Quantity: `10`
  - *Talking point:* "The item is successfully inserted into the database. Let's verify by viewing all items (Type 1) — Pilates Ball is now present."

#### Step 4: Demonstrate UPDATE (Option 4)
- **Type:** `4` (Update Equipment)
  - Enter Equipment ID to Update: `701`
  - Enter New Equipment Name: `Basketball Pro`
  - Enter New Equipment Type: `Sports`
  - Enter New Total Quantity: `20`
  - *Talking point:* "The system updates the record and automatically adjusts Available Quantity using delta calculation ($Available = Available + [NewTotal - OldTotal]$)."

#### Step 5: Demonstrate DELETE (Option 5) — Two Scenarios
- **Scenario A (Referential Integrity Protection):**
  - **Type:** `5` (Delete Equipment)
  - Enter Equipment ID to Delete: `701`
  - *Talking point:* "Notice the system blocks the deletion with an informative message: `Cannot delete or modify: equipment is currently referenced in reservations.` MySQL Foreign Key Error 1451 is caught gracefully."
- **Scenario B (Successful Deletion):**
  - **Type:** `5` (Delete Equipment)
  - Enter Equipment ID to Delete: `[ID of Pilates Ball, e.g. 709]`
  - *Talking point:* "Unreferenced equipment deletes cleanly."
- **Type:** `0` to return to the Main Menu.

#### Step 6: Demonstrate 2–3 Complex Queries (Submenu 2)
- At Main Menu: **Type:** `2` (Reports & Queries)
- **Query 2 (Multi-Table JOIN): Type `2`**
  - *Talking point:* "This query performs an `INNER JOIN` between `RESERVATION` and `RESERVER` and a `LEFT JOIN` on `STAFF` using `COALESCE` to display 'Unassigned' for pending student requests."
- **Query 4 (Subquery with `NOT IN`): Type `4`**
  - *Talking point:* "This subquery isolates stagnant inventory by identifying equipment IDs that have never appeared in `RESERVATION_EQUIPMENT` (Jump Rope and Stopwatch)."
- **Query 6 (`GROUP BY` + `HAVING`): Type `6`**
  - *Talking point:* "This query groups reservations by reserver and filters for frequent users with `HAVING COUNT(r.ReservationID) > 1`."
- *(Optional bonus):* **Type `9`** to show the batch execution of all 8 queries in one click!
- **Type:** `0` to return to Main Menu.

#### Step 7: Demonstrate EXIT
- At Main Menu: **Type:** `0`
- *Talking point:* "The system gracefully closes database streams and displays our exit greeting: `Thank you for using P.E.E.R.S. Goodbye!`"

---

## 3. Anticipated Defense Questions & Model Answers

### Q1: Why did you model `RESERVER` and `EQUIPMENT` as Supertype/Subtype hierarchies instead of flat tables?
> **Answer:** "In our domain, students and professors share common attributes (name, email) but possess distinct identification rules and policies: professors have employee IDs and faculty affiliations, and can reserve multi-item class packages, whereas students are restricted to single-item bookings. By using a disjoint, total supertype `RESERVER`, we eliminate nulls and redundancy. Similarly, for `EQUIPMENT`, we used an overlapping supertype because some athletic gear—like jump ropes—serves both general fitness and competitive sports training."

### Q2: How does the system prevent SQL Injection attacks?
> **Answer:** "Every database interaction in `EquipmentDAO.java` and `QueryDAO.java` strictly utilizes Java `PreparedStatement` with parameterized placeholders (`?`). No SQL strings are concatenated with user input. In addition, `InputHelper.java` performs type checking and input sanitization before queries are executed."

### Q3: What happens to reservation records when an equipment item or a reserver is deleted?
> **Answer:** "We configured referential integrity actions to protect historical audits:
> - If a `RESERVER` is deleted, their subtype (`STUDENT`/`PROFESSOR`) cascades (`ON DELETE CASCADE`), but the parent `RESERVATION` is protected (`ON DELETE RESTRICT`) to preserve facility records.
> - In `RESERVATION_EQUIPMENT`, the equipment foreign key uses `RESTRICT`. If an admin attempts to delete equipment currently on loan or reserved in past records, MySQL raises error 1451, which our DAO catches and displays gracefully to the user."

### Q4: Why is your schema in 3NF and not 2NF or BCNF?
> **Answer:** 
> - **1NF:** All repeating groups (such as multiple equipment items per booking) are separated into the associative table `RESERVATION_EQUIPMENT`, ensuring every column is atomic.
> - **2NF:** In `RESERVATION_EQUIPMENT`, the non-key attribute `QuantityReserved` depends on the full composite key `(ReservationID, EquipmentID)`. No partial dependencies exist.
> - **3NF:** All transitive dependencies are removed; for example, faculty departmental names are stored in `FACULTY` rather than inside `PROFESSOR`, ensuring every non-key column depends solely on the primary key."

### Q5: How do you handle concurrency or double-booking of equipment?
> **Answer:** "At the schema level, we enforce domain integrity checks (`CHECK (AvailableQuantity >= 0 AND AvailableQuantity <= TotalQuantity)`). When a reservation is approved, the application decrements available stock. If stock reaches zero, the item is flagged as out of stock, preventing subsequent bookings for that overlapping time slot."
