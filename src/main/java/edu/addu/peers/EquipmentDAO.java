package edu.addu.peers;

import java.sql.*;

public class EquipmentDAO {

    public static void viewAll() {
        String sql = "SELECT * FROM EQUIPMENT ORDER BY EquipmentID";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printTableDivider();
            System.out.printf("| %-4s | %-25s | %-10s | %-15s |%n", "ID", "Name", "Type", "Available/Total");
            printTableDivider();

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                String stock = rs.getInt("AvailableQuantity") + " / " + rs.getInt("TotalQuantity");
                System.out.printf("| %-4d | %-25s | %-10s | %-15s |%n",
                        rs.getInt("EquipmentID"),
                        rs.getString("EquipmentTypeName"),
                        rs.getString("EquipmentType"),
                        stock);
            }

            if (!hasRows) {
                System.out.println("| No equipment records found.                                           |");
            }
            printTableDivider();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void searchById(int id) {
        String sql = "SELECT * FROM EQUIPMENT WHERE EquipmentID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    printTableDivider();
                    System.out.printf("| %-4s | %-25s | %-10s | %-15s |%n", "ID", "Name", "Type", "Available/Total");
                    printTableDivider();
                    String stock = rs.getInt("AvailableQuantity") + " / " + rs.getInt("TotalQuantity");
                    System.out.printf("| %-4d | %-25s | %-10s | %-15s |%n",
                            rs.getInt("EquipmentID"),
                            rs.getString("EquipmentTypeName"),
                            rs.getString("EquipmentType"),
                            stock);
                    printTableDivider();
                } else {
                    System.out.println("No equipment found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static Equipment getById(int id) {
        String sql = "SELECT * FROM EQUIPMENT WHERE EquipmentID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Equipment(
                            rs.getInt("EquipmentID"),
                            rs.getString("EquipmentTypeName"),
                            rs.getString("EquipmentType"),
                            rs.getInt("TotalQuantity"),
                            rs.getInt("AvailableQuantity")
                    );
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    public static void add(Equipment equipment) {
        String sql = "INSERT INTO EQUIPMENT "
                + "(EquipmentTypeName, TotalQuantity, AvailableQuantity, EquipmentType) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, equipment.getEquipmentTypeName());
            ps.setInt(2, equipment.getTotalQuantity());
            ps.setInt(3, equipment.getAvailableQuantity());
            ps.setString(4, equipment.getEquipmentType());
            ps.executeUpdate();
            System.out.println("Equipment added successfully.");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void update(Equipment equipment) {
        String sql = "UPDATE EQUIPMENT SET "
                + "EquipmentTypeName = ?, "
                + "EquipmentType = ?, "
                + "AvailableQuantity = AvailableQuantity + (? - TotalQuantity), "
                + "TotalQuantity = ? "
                + "WHERE EquipmentID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, equipment.getEquipmentTypeName());
            ps.setString(2, equipment.getEquipmentType());
            ps.setInt(3, equipment.getTotalQuantity());
            ps.setInt(4, equipment.getTotalQuantity());
            ps.setInt(5, equipment.getEquipmentId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Equipment updated successfully.");
            } else {
                System.out.println("No equipment found with ID: " + equipment.getEquipmentId());
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM EQUIPMENT WHERE EquipmentID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Equipment deleted successfully.");
            } else {
                System.out.println("No equipment found with ID: " + id);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private static void printTableDivider() {
        System.out.println("+------+---------------------------+------------+-----------------+");
    }

    private static void handleSQLException(SQLException e) {
        if (e.getErrorCode() == 1451) {
            System.out.println("Database Error: Cannot delete or modify: equipment is currently referenced in reservations.");
        } else if (e.getErrorCode() == 1062) {
            System.out.println("Database Error: An equipment item with this name already exists.");
        } else if (e.getErrorCode() == 3819) {
            System.out.println("Database Error: Invalid quantity: available units must be between 0 and total quantity.");
        } else {
            System.out.println("Database Error (" + e.getErrorCode() + "): " + e.getMessage());
        }
    }
}
