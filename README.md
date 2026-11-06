# P.E.E.R.S. (Physical Exercise Equipment Reservation System)

A Java console application for managing physical exercise equipment reservations and running inventory reports.

---

## Prerequisites
- **Java Development Kit (JDK)**: Version 21 or newer
- **MySQL Server** (or XAMPP / MariaDB / Docker MySQL)
- **Visual Studio Code** with the **Extension Pack for Java** installed

---

## Getting Started

### 1. Database Setup
Before running the application, set up the MySQL database:
1. Ensure your local MySQL server is running on port `3306`.
2. Execute the schema script:
   - Run [`database/schema.sql`](database/schema.sql) in MySQL Workbench, phpMyAdmin, or your MySQL CLI.
   - *(Optional)* Run [`database/sample_data.sql`](database/sample_data.sql) to seed test equipment and reserver records.

### 2. Database Credentials
The application connects to MySQL using the following defaults:
- **Host**: `localhost:3306`
- **Database**: `peers`
- **User**: `root` (default)
- **Password**: `root` (default)

If your local MySQL uses different credentials (e.g. blank password `""` on standard XAMPP):
- Open [`.vscode/launch.json`](.vscode/launch.json) and update the `env` values (`DB_USER` and `DB_PASSWORD`), **or**
- Set `DB_USER` and `DB_PASSWORD` as system environment variables.

### 3. Running the Application in VS Code
1. Open the project folder in VS Code.
2. In the Run & Debug tab (or pressing `F5`), select **Main** to start the app.
3. You can also run **DBConnection** to quickly verify connectivity to MySQL.
