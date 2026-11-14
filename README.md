# P.E.E.R.S. (Physical Exercise Equipment Reservation System)

**Course**: Information Management  
**Institution**: Ateneo de Davao University  
**Class Code**: 4-310 | **Group**: Group 5  
**Milestone**: Week 5 – Final Project, Testing & Documentation  

---

## 1. Project Overview

**P.E.E.R.S.** is a centralized console-based management system designed for school physical education departments and sports coordinators. It replaces manual, error-prone paper/ID-based equipment lending with a relational database-backed solution supporting real-time inventory tracking, multi-equipment reservations, user management (students and professors), and analytical operational reporting.

---

## 2. Deliverables & Project Structure

```text
peers/
├── .vscode/
│   ├── launch.json              # VS Code run configurations with DB environment variables
│   └── settings.json
├── database/
│   ├── database.sql             # Unified final schema + seed dataset (Week 5 Deliverable)
│   ├── schema.sql               # DDL table creation script
│   └── sample_data.sql          # DML seed records
├── lib/
│   └── mysql-connector-j-8.4.0.jar # Standalone MySQL JDBC driver (for offline compilation)
├── src/
│   ├── main/java/edu/addu/peers/
│   │   ├── DBConnection.java    # Database connection manager with environment fallbacks
│   │   ├── Equipment.java       # Equipment entity domain model
│   │   ├── EquipmentDAO.java    # CRUD operations for Equipment table
│   │   ├── InputHelper.java     # Robust user input validation and boundary checking
│   │   ├── Main.java            # Interactive console menu driver
│   │   └── QueryDAO.java        # Week 4 analytical reports & advanced SQL queries (Q1-Q8)
│   └── test/java/edu/addu/peers/
│       └── AppTest.java         # Base unit test suite
├── database.sql                 # Root copy of the unified database script
├── pom.xml                      # Maven project configuration (MySQL connector & JUnit)
└── README.md                    # Setup, running, and demonstration instructions
```

---

## 3. Prerequisites

- **Java Development Kit (JDK)**: Version 21 or newer (Java 21 LTS verified).
- **MySQL Server**: Version 8.0 or newer (MySQL Workbench, XAMPP, MariaDB, or Docker MySQL).
- *(Optional)* **Visual Studio Code** with the *Extension Pack for Java*.
- *(Optional)* **Apache Maven** (if building with Maven).

---

## 4. Database Setup

The database can be configured in one step using the final unified script [`database.sql`](database.sql):

### Option A: Using MySQL Command-Line Client
```bash
mysql -u root -p < database.sql
```

### Option B: Using MySQL Workbench
1. Open MySQL Workbench and connect to your local MySQL instance.
2. Go to **File** $\rightarrow$ **Open SQL Script...** and select `database.sql` (or `database/database.sql`).
3. Click the **Execute (lightning bolt)** button to run the entire script.
4. Refresh the schema navigator; you will see the `peers` database populated with 10 tables and initial seed data.

### Option C: Using phpMyAdmin (XAMPP)
1. Navigate to `http://localhost/phpmyadmin`.
2. Click **Import** in the top navigation bar.
3. Choose `database.sql` and click **Import**.

---

## 5. Database Credentials & Configuration

`DBConnection.java` connects to MySQL using sensible defaults and supports environment variables:

| Setting | Default Value | Environment Variable Override |
| :--- | :--- | :--- |
| **URL** | `jdbc:mysql://localhost:3306/peers` | `DB_URL` |
| **Username** | `root` | `DB_USER` |
| **Password** | `root` | `DB_PASSWORD` |

- If you use XAMPP with no root password, edit [`.vscode/launch.json`](.vscode/launch.json) to set `"DB_PASSWORD": ""` or configure your terminal environment variables:
  ```powershell
  $env:DB_USER="root"
  $env:DB_PASSWORD=""
  ```

---

## 6. How to Run the Application

### Method 1: In Visual Studio Code (Recommended)
1. Open the `peers` folder in VS Code.
2. Open the **Run and Debug** view (`Ctrl + Shift + D` or `Cmd + Shift + D`).
3. Select **Main** from the configuration dropdown and press `F5` (or click Run).
4. *(Optional)* Select **DBConnection** to perform a quick connectivity check.

### Method 2: Terminal / Command Prompt (Direct Java Execution)
You can compile and run directly using the included JDBC jar in `lib/`:

**Windows (PowerShell or CMD):**
```powershell
# Compile all source files into target/classes
javac -cp "lib/mysql-connector-j-8.4.0.jar" -d target/classes src/main/java/edu/addu/peers/*.java

# Run the console application
java -cp "target/classes;lib/mysql-connector-j-8.4.0.jar" edu.addu.peers.Main
```

**macOS / Linux:**
```bash
javac -cp "lib/mysql-connector-j-8.4.0.jar" -d target/classes src/main/java/edu/addu/peers/*.java
java -cp "target/classes:lib/mysql-connector-j-8.4.0.jar" edu.addu.peers.Main
```

### Method 3: Using Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.addu.peers.Main"
```

---

## 7. Interactive Menu Walkthrough

When launched, the application displays the main menu:

```text
==========================================
   P.E.E.R.S. - Physical Exercise
   Equipment Reservation System
==========================================
1. Equipment Management (CRUD)
2. Reports & Queries (Week 4)
0. Exit Application
```

### Submenu 1: Equipment Management (CRUD)
- **1. View All Equipment**: Displays all inventory with current and total quantities formatted in a clean table.
- **2. Search Equipment by ID**: Retrieves a specific equipment record by its primary key.
- **3. Add New Equipment (INSERT)**: Prompts for equipment name, category (`Sports`, `Fitness`, `Both`), and total quantity.
- **4. Update Equipment (UPDATE)**: Updates equipment metadata and recalculates available inventory.
- **5. Delete Equipment (DELETE)**: Deletes an equipment record. If the equipment is actively tied to reservations, an informative foreign key constraint message is displayed without crashing.
- **0. Back to Main Menu**

### Submenu 2: Reports & Queries
- **1. Out-of-Stock Equipment** (`WHERE AvailableQuantity = 0`)
- **2. Scheduled Reservations with Details** (`JOIN` across `RESERVATION`, `RESERVER`, and `STAFF`)
- **3. Total Reservations per Reserver** (`COUNT` + `GROUP BY`)
- **4. Equipment Never Reserved** (`NOT IN` subquery)
- **5. Total Units Reserved per Category** (`SUM` + `GROUP BY`)
- **6. Frequent Reservers** (`HAVING COUNT > 1`)
- **7. Top 5 Most Reserved Equipment** (`ORDER BY` + `LIMIT 5`)
- **8. Average Stock per Category** (`AVG` + `GROUP BY`)
- **9. Run All 8 Queries**: Executes the entire query suite sequentially for quick demonstration and screenshot capture.
- **0. Back to Main Menu**

---

## 8. Group Members (Group 5)

- Dantes, Terenz Jahred C.
- Deza, Micko Dwayne N.
- Fukuoka, Kazuki
- Lambino, Brent Angelo
- Leonardo, Precious Danica
- Miguel, Steven Gabriel G.
- Pulido, Miami Patrice, M.
- Sayaman, Von Najib P.
