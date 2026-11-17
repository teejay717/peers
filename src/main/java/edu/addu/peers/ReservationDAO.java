package edu.addu.peers;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDAO {

    public static class ItemRequest {
        public int equipmentId;
        public int quantity;

        public ItemRequest(int equipmentId, int quantity) {
            this.equipmentId = equipmentId;
            this.quantity = quantity;
        }
    }

    public static String[] getReserverInfo(int reserverId) {
        String sql = "SELECT CONCAT(FirstName, ' ', LastName) AS Name, ReserverType FROM RESERVER WHERE ReserverID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reserverId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[] { rs.getString("Name"), rs.getString("ReserverType") };
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Displays all reservations with reserver, equipment line items, and status.
     */
    public static void viewAll() {
        String sql = "SELECT r.ReservationID, "
                   + "       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName, "
                   + "       rv.ReserverType, "
                   + "       eq.EquipmentTypeName, "
                   + "       re.QuantityReserved, "
                   + "       r.ReservationDate, "
                   + "       CONCAT(DATE_FORMAT(r.StartTime, '%H:%i'), '-', DATE_FORMAT(r.EndTime, '%H:%i')) AS TimeSlot, "
                   + "       r.ReservationStatus "
                   + "FROM RESERVATION r "
                   + "JOIN RESERVER rv ON r.ReserverID = rv.ReserverID "
                   + "JOIN RESERVATION_EQUIPMENT re ON r.ReservationID = re.ReservationID "
                   + "JOIN EQUIPMENT eq ON re.EquipmentID = eq.EquipmentID "
                   + "ORDER BY r.ReservationID DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printTableDivider();
            System.out.printf("| %-4s | %-20s | %-9s | %-20s | %-4s | %-10s | %-11s | %-9s |%n",
                    "ID", "Reserver", "Type", "Equipment", "Qty", "Date", "Time Slot", "Status");
            printTableDivider();

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-20s | %-9s | %-20s | %-4d | %-10s | %-11s | %-9s |%n",
                        rs.getInt("ReservationID"),
                        rs.getString("ReserverName"),
                        rs.getString("ReserverType"),
                        rs.getString("EquipmentTypeName"),
                        rs.getInt("QuantityReserved"),
                        rs.getString("ReservationDate"),
                        rs.getString("TimeSlot"),
                        rs.getString("ReservationStatus"));
            }

            if (!hasRows) {
                System.out.println("| No reservation records found.                                                                           |");
            }
            printTableDivider();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Displays only active (Approved / Pending) reservations that are eligible to be returned.
     */
    public static void viewActive() {
        String sql = "SELECT r.ReservationID, "
                   + "       CONCAT(rv.FirstName, ' ', rv.LastName) AS ReserverName, "
                   + "       rv.ReserverType, "
                   + "       eq.EquipmentTypeName, "
                   + "       re.QuantityReserved, "
                   + "       r.ReservationDate, "
                   + "       CONCAT(DATE_FORMAT(r.StartTime, '%H:%i'), '-', DATE_FORMAT(r.EndTime, '%H:%i')) AS TimeSlot, "
                   + "       r.ReservationStatus "
                   + "FROM RESERVATION r "
                   + "JOIN RESERVER rv ON r.ReserverID = rv.ReserverID "
                   + "JOIN RESERVATION_EQUIPMENT re ON r.ReservationID = re.ReservationID "
                   + "JOIN EQUIPMENT eq ON re.EquipmentID = eq.EquipmentID "
                   + "WHERE r.ReservationStatus IN ('Approved', 'Pending') "
                   + "ORDER BY r.ReservationID DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printTableDivider();
            System.out.printf("| %-4s | %-20s | %-9s | %-20s | %-4s | %-10s | %-11s | %-9s |%n",
                    "ID", "Reserver", "Type", "Equipment", "Qty", "Date", "Time Slot", "Status");
            printTableDivider();

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("| %-4d | %-20s | %-9s | %-20s | %-4d | %-10s | %-11s | %-9s |%n",
                        rs.getInt("ReservationID"),
                        rs.getString("ReserverName"),
                        rs.getString("ReserverType"),
                        rs.getString("EquipmentTypeName"),
                        rs.getInt("QuantityReserved"),
                        rs.getString("ReservationDate"),
                        rs.getString("TimeSlot"),
                        rs.getString("ReservationStatus"));
            }

            if (!hasRows) {
                System.out.println("| No active reservations currently borrowed.                                                             |");
            }
            printTableDivider();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    /**
     * Creates a new reservation with multiple equipment items inside an atomic ACID transaction.
     */
    public static void createReservation(int reserverId, List<ItemRequest> items,
                                         String date, String startTime, String endTime) {
        if (items == null || items.isEmpty()) {
            System.out.println("Error: No equipment items specified for reservation.");
            return;
        }

        String checkReserverSql = "SELECT CONCAT(FirstName, ' ', LastName) AS Name, ReserverType FROM RESERVER WHERE ReserverID = ?";
        String checkStockSql    = "SELECT EquipmentTypeName, AvailableQuantity FROM EQUIPMENT WHERE EquipmentID = ? FOR UPDATE";
        String insertHeaderSql  = "INSERT INTO RESERVATION (ReserverID, StaffID, ReservationDate, StartTime, EndTime, ReservationStatus) "
                                + "VALUES (?, 10001, ?, ?, ?, 'Approved')";
        String insertLineSql    = "INSERT INTO RESERVATION_EQUIPMENT (ReservationID, EquipmentID, QuantityReserved) VALUES (?, ?, ?)";
        String deductStockSql   = "UPDATE EQUIPMENT SET AvailableQuantity = AvailableQuantity - ? WHERE EquipmentID = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin ACID Transaction

            // 1. Verify reserver exists
            String reserverName;
            try (PreparedStatement ps = conn.prepareStatement(checkReserverSql)) {
                ps.setInt(1, reserverId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Error: Reserver with ID " + reserverId + " does not exist.");
                        conn.rollback();
                        return;
                    }
                    reserverName = rs.getString("Name");
                }
            }

            // 2. Verify all items and check stock availability
            Map<Integer, String> itemNames = new HashMap<>();
            for (ItemRequest item : items) {
                if (item.quantity <= 0) {
                    System.out.println("Error: Quantity must be greater than zero.");
                    conn.rollback();
                    return;
                }
                try (PreparedStatement ps = conn.prepareStatement(checkStockSql)) {
                    ps.setInt(1, item.equipmentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Error: Equipment with ID " + item.equipmentId + " does not exist.");
                            conn.rollback();
                            return;
                        }
                        String eqName = rs.getString("EquipmentTypeName");
                        int available = rs.getInt("AvailableQuantity");
                        if (available < item.quantity) {
                            System.out.printf("Error: Insufficient stock. '%s' only has %d unit(s) available.%n", eqName, available);
                            conn.rollback();
                            return;
                        }
                        itemNames.put(item.equipmentId, eqName);
                    }
                }
            }

            // 3. Insert reservation header (ONCE)
            int newReservationId;
            try (PreparedStatement ps = conn.prepareStatement(insertHeaderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, reserverId);
                ps.setString(2, date);
                ps.setString(3, startTime);
                ps.setString(4, endTime);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    newReservationId = rs.getInt(1);
                }
            }

            // 4. Insert each equipment line item and deduct stock under newReservationId
            StringBuilder summary = new StringBuilder();
            for (ItemRequest item : items) {
                try (PreparedStatement ps = conn.prepareStatement(insertLineSql)) {
                    ps.setInt(1, newReservationId);
                    ps.setInt(2, item.equipmentId);
                    ps.setInt(3, item.quantity);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(deductStockSql)) {
                    ps.setInt(1, item.quantity);
                    ps.setInt(2, item.equipmentId);
                    ps.executeUpdate();
                }
                summary.append(String.format("%n  - %d x %s", item.quantity, itemNames.get(item.equipmentId)));
            }

            conn.commit(); // Atomically commits header + all line items + stock deductions
            System.out.println("\n[SUCCESS] Reservation #" + newReservationId + " approved for " + reserverName + "!");
            System.out.println("Items Reserved:" + summary);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    /* ignored */
                }
            }
            System.out.println("Database Error during reservation: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    /**
     * Single-item convenience overload.
     */
    public static void createReservation(int reserverId, int equipmentId, int quantity,
                                         String date, String startTime, String endTime) {
        List<ItemRequest> list = new ArrayList<>();
        list.add(new ItemRequest(equipmentId, quantity));
        createReservation(reserverId, list, date, startEndTimeWrapper(startTime), endTime);
    }

    private static String startEndTimeWrapper(String s) {
        return s;
    }

    /**
     * Marks a reservation as 'Completed' and restores borrowed equipment to available inventory.
     */
    public static void returnEquipment(int reservationId) {
        String checkSql        = "SELECT ReservationStatus FROM RESERVATION WHERE ReservationID = ? FOR UPDATE";
        String lineSql         = "SELECT re.EquipmentID, re.QuantityReserved, eq.EquipmentTypeName "
                               + "FROM RESERVATION_EQUIPMENT re "
                               + "JOIN EQUIPMENT eq ON re.EquipmentID = eq.EquipmentID "
                               + "WHERE re.ReservationID = ?";
        String updateStatusSql = "UPDATE RESERVATION SET ReservationStatus = 'Completed' WHERE ReservationID = ?";
        String restoreStockSql = "UPDATE EQUIPMENT SET AvailableQuantity = AvailableQuantity + ? WHERE EquipmentID = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin ACID Transaction

            // 1. Verify reservation status
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, reservationId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Error: Reservation #" + reservationId + " not found.");
                        conn.rollback();
                        return;
                    }
                    String status = rs.getString("ReservationStatus");
                    if ("Completed".equalsIgnoreCase(status)) {
                        System.out.println("Notice: Reservation #" + reservationId + " is already marked as Completed (equipment already returned).");
                        conn.rollback();
                        return;
                    }
                    if (!"Approved".equalsIgnoreCase(status) && !"Pending".equalsIgnoreCase(status)) {
                        System.out.println("Cannot return: Reservation status is '" + status + "'.");
                        conn.rollback();
                        return;
                    }
                }
            }

            // 2. Fetch line items and restore inventory
            StringBuilder summary = new StringBuilder();
            try (PreparedStatement ps = conn.prepareStatement(lineSql)) {
                ps.setInt(1, reservationId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int eqId = rs.getInt("EquipmentID");
                        int qty  = rs.getInt("QuantityReserved");
                        String name = rs.getString("EquipmentTypeName");

                        try (PreparedStatement updatePs = conn.prepareStatement(restoreStockSql)) {
                            updatePs.setInt(1, qty);
                            updatePs.setInt(2, eqId);
                            updatePs.executeUpdate();
                        }
                        summary.append(String.format(" +%d %s", qty, name));
                    }
                }
            }

            // 3. Mark reservation as Completed
            try (PreparedStatement ps = conn.prepareStatement(updateStatusSql)) {
                ps.setInt(1, reservationId);
                ps.executeUpdate();
            }

            conn.commit(); // Commit status update and inventory restoration
            System.out.println("\n[SUCCESS] Reservation #" + reservationId + " marked as COMPLETED!");
            System.out.println("Equipment safely returned to stock:" + summary);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    /* ignored */
                }
            }
            System.out.println("Database Error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
    }

    private static void printTableDivider() {
        System.out.println("+------+----------------------+-----------+----------------------+------+------------+-------------+-----------+");
    }
}
