package edu.addu.peers;

import java.sql.*;

public class QueryDAO {

    /**
     * Query 1: WHERE
     * Business Question: Which equipment items are currently completely out of stock (AvailableQuantity = 0)?
     */
    public static void query1_OutOfStockEquipment() {
        String sql = "SELECT EquipmentID, EquipmentTypeName, EquipmentType, TotalQuantity, AvailableQuantity "
                   + "FROM EQUIPMENT "
                   + "WHERE AvailableQuantity = 0 "
                   + "ORDER BY EquipmentID";

        System.out.println("\n--- Query 1: Out-of-Stock Equipment (WHERE) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(68);
            System.out.printf("| %-4s | %-25s | %-10s | %-7s | %-9s |%n",
                    "ID", "Equipment Name", "Type", "Total", "Available");
            printDivider(68);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-25s | %-10s | %-7d | %-9d |%n",
                        rs.getInt("EquipmentID"),
                        rs.getString("EquipmentTypeName"),
                        rs.getString("EquipmentType"),
                        rs.getInt("TotalQuantity"),
                        rs.getInt("AvailableQuantity"));
            }

            if (!hasRows) {
                System.out.println("| All equipment items currently have available stock.               |");
            }
            printDivider(68);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 2: JOIN
     * Business Question: Which reservations are currently scheduled, who requested them, and which staff member reviewed them?
     */
    public static void query2_ScheduledReservations() {
        String sql = "SELECT r.ReservationID, "
                   + "       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName, "
                   + "       rv.ReserverType, "
                   + "       r.ReservationDate, "
                   + "       r.StartTime, "
                   + "       r.EndTime, "
                   + "       r.ReservationStatus, "
                   + "       COALESCE(s.StaffName, 'Unassigned') AS StaffName "
                   + "FROM RESERVATION r "
                   + "JOIN RESERVER rv ON r.ReserverID = rv.ReserverID "
                   + "LEFT JOIN STAFF s ON r.StaffID = s.StaffID "
                   + "ORDER BY r.ReservationDate DESC";

        System.out.println("\n--- Query 2: Scheduled Reservations with Reserver and Staff (JOIN) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(103);
            System.out.printf("| %-4s | %-20s | %-10s | %-10s | %-11s | %-10s | %-20s |%n",
                    "ID", "Reserver", "Type", "Date", "Time Slot", "Status", "Reviewing Staff");
            printDivider(103);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                String timeSlot = rs.getString("StartTime").substring(0, 5) + "-" + rs.getString("EndTime").substring(0, 5);
                System.out.printf("| %-4d | %-20s | %-10s | %-10s | %-11s | %-10s | %-20s |%n",
                        rs.getInt("ReservationID"),
                        rs.getString("ReserverName"),
                        rs.getString("ReserverType"),
                        rs.getDate("ReservationDate").toString(),
                        timeSlot,
                        rs.getString("ReservationStatus"),
                        rs.getString("StaffName"));
            }

            if (!hasRows) {
                System.out.println("| No reservations found.                                                                              |");
            }
            printDivider(103);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 3: JOIN + COUNT + GROUP BY
     * Business Question: How many reservations has each reserver submitted?
     */
    public static void query3_ReservationsPerReserver() {
        String sql = "SELECT rv.ReserverID, "
                   + "       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName, "
                   + "       rv.ReserverType, "
                   + "       COUNT(r.ReservationID) AS TotalReservations "
                   + "FROM RESERVER rv "
                   + "JOIN RESERVATION r ON rv.ReserverID = r.ReserverID "
                   + "GROUP BY rv.ReserverID, rv.FirstName, rv.LastName, rv.ReserverType "
                   + "ORDER BY TotalReservations DESC";

        System.out.println("\n--- Query 3: Total Reservations Submitted by Each Reserver (JOIN + COUNT + GROUP BY) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(66);
            System.out.printf("| %-4s | %-25s | %-11s | %-14s |%n",
                    "ID", "Reserver Name", "User Type", "Reservations");
            printDivider(66);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-25s | %-11s | %-14d |%n",
                        rs.getInt("ReserverID"),
                        rs.getString("ReserverName"),
                        rs.getString("ReserverType"),
                        rs.getInt("TotalReservations"));
            }

            if (!hasRows) {
                System.out.println("| No reservation activity found.                                 |");
            }
            printDivider(66);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 4: Subquery
     * Business Question: Which equipment items have never been reserved?
     */
    public static void query4_NeverReservedEquipment() {
        String sql = "SELECT EquipmentID, EquipmentTypeName, EquipmentType, TotalQuantity "
                   + "FROM EQUIPMENT "
                   + "WHERE EquipmentID NOT IN ("
                   + "    SELECT DISTINCT EquipmentID FROM RESERVATION_EQUIPMENT"
                   + ") "
                   + "ORDER BY EquipmentID";

        System.out.println("\n--- Query 4: Equipment Never Reserved (Subquery) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(56);
            System.out.printf("| %-4s | %-25s | %-10s | %-5s |%n",
                    "ID", "Equipment Name", "Type", "Stock");
            printDivider(56);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-25s | %-10s | %-5d |%n",
                        rs.getInt("EquipmentID"),
                        rs.getString("EquipmentTypeName"),
                        rs.getString("EquipmentType"),
                        rs.getInt("TotalQuantity"));
            }

            if (!hasRows) {
                System.out.println("| All equipment items have been reserved at least once.  |");
            }
            printDivider(56);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 5: JOIN + SUM + GROUP BY
     * Business Question: What is the total quantity of equipment reserved for each equipment type?
     */
    public static void query5_TotalQuantityPerType() {
        String sql = "SELECT e.EquipmentType, "
                   + "       SUM(re.QuantityReserved) AS TotalUnitsReserved "
                   + "FROM EQUIPMENT e "
                   + "JOIN RESERVATION_EQUIPMENT re ON e.EquipmentID = re.EquipmentID "
                   + "GROUP BY e.EquipmentType "
                   + "ORDER BY TotalUnitsReserved DESC";

        System.out.println("\n--- Query 5: Total Units Reserved per Equipment Type (JOIN + SUM + GROUP BY) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(42);
            System.out.printf("| %-15s | %-20s |%n", "Equipment Type", "Total Units Reserved");
            printDivider(42);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-15s | %-20d |%n",
                        rs.getString("EquipmentType"),
                        rs.getInt("TotalUnitsReserved"));
            }

            if (!hasRows) {
                System.out.println("| No reservation line items found.       |");
            }
            printDivider(42);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 6: GROUP BY + HAVING
     * Business Question: Which reservers have submitted more than 1 reservation?
     */
    public static void query6_FrequentReservers() {
        String sql = "SELECT rv.ReserverID, "
                   + "       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName, "
                   + "       rv.EmailAddress, "
                   + "       COUNT(r.ReservationID) AS ReservationCount "
                   + "FROM RESERVER rv "
                   + "JOIN RESERVATION r ON rv.ReserverID = r.ReserverID "
                   + "GROUP BY rv.ReserverID, rv.FirstName, rv.LastName, rv.EmailAddress "
                   + "HAVING COUNT(r.ReservationID) > 1 "
                   + "ORDER BY ReservationCount DESC";

        System.out.println("\n--- Query 6: Reservers with More Than 1 Reservation (GROUP BY + HAVING) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(78);
            System.out.printf("| %-4s | %-22s | %-28s | %-12s |%n",
                    "ID", "Reserver Name", "Email", "Reservations");
            printDivider(78);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-22s | %-28s | %-12d |%n",
                        rs.getInt("ReserverID"),
                        rs.getString("ReserverName"),
                        rs.getString("EmailAddress"),
                        rs.getInt("ReservationCount"));
            }

            if (!hasRows) {
                System.out.println("| No reserver has more than 1 reservation yet.                               |");
            }
            printDivider(78);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 7: ORDER BY + LIMIT
     * Business Question: What are the top 5 most frequently reserved equipment items by total quantity?
     */
    public static void query7_Top5ReservedEquipment() {
        String sql = "SELECT e.EquipmentID, "
                   + "       e.EquipmentTypeName, "
                   + "       SUM(re.QuantityReserved) AS TotalQuantityReserved "
                   + "FROM EQUIPMENT e "
                   + "JOIN RESERVATION_EQUIPMENT re ON e.EquipmentID = re.EquipmentID "
                   + "GROUP BY e.EquipmentID, e.EquipmentTypeName "
                   + "ORDER BY TotalQuantityReserved DESC "
                   + "LIMIT 5";

        System.out.println("\n--- Query 7: Top 5 Most Reserved Equipment Items (ORDER BY + LIMIT) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(60);
            System.out.printf("| %-4s | %-25s | %-22s |%n",
                    "ID", "Equipment Name", "Total Quantity Borrowed");
            printDivider(60);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-25s | %-22d |%n",
                        rs.getInt("EquipmentID"),
                        rs.getString("EquipmentTypeName"),
                        rs.getInt("TotalQuantityReserved"));
            }

            if (!hasRows) {
                System.out.println("| No equipment reservation data found.                     |");
            }
            printDivider(60);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Query 8: AVG + GROUP BY
     * Business Question: What is the average total stock per equipment category?
     */
    public static void query8_AverageStockPerCategory() {
        String sql = "SELECT EquipmentType, "
                   + "       ROUND(AVG(TotalQuantity), 1) AS AvgTotalStock "
                   + "FROM EQUIPMENT "
                   + "GROUP BY EquipmentType "
                   + "ORDER BY AvgTotalStock DESC";

        System.out.println("\n--- Query 8: Average Stock Quantity per Equipment Type (AVG + GROUP BY) ---");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printDivider(38);
            System.out.printf("| %-15s | %-16s |%n", "Equipment Type", "Avg Stock Level");
            printDivider(38);

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-15s | %-16.1f |%n",
                        rs.getString("EquipmentType"),
                        rs.getDouble("AvgTotalStock"));
            }

            if (!hasRows) {
                System.out.println("| No equipment found.                |");
            }
            printDivider(38);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void printDivider(int width) {
        StringBuilder sb = new StringBuilder("+");
        for (int i = 0; i < width - 2; i++) {
            sb.append("-");
        }
        sb.append("+");
        System.out.println(sb.toString());
    }
}
