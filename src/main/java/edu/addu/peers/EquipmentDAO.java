package edu.addu.peers;

import java.sql.*;

public class EquipmentDAO {
    public static void viewAll() {
        String sql = "SELECT * FROM EQUIPMENT ORDER BY EquipmentID";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
                
                System.out.println("ID | Name | Type | Available/Total");
                while (rs.next()) {
                System.out.println(rs.getInt("EquipmentID") + " | "
                        + rs.getString("EquipmentTypeName") + " | "
                        + rs.getString("EquipmentType") + " | "
                        + rs.getInt("AvailableQuantity") + "/"
                        + rs.getInt("TotalQuantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void searchById(int id) {
        String sql = "SELECT * FROM EQUIPMENT WHERE EquipmentID = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
            ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getInt("EquipmentID") + " | "
                            + rs.getString("EquipmentTypeName") + " | "
                            + rs.getString("EquipmentType") + " | "
                            + rs.getInt("AvailableQuantity") + "/"
                            + rs.getInt("TotalQuantity"));
                
                } else {
                    System.out.println("No equipment found with the ID: " + id + ".");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
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
            System.out.println("Equipment added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void update(Equipment equipment) {
        String sql = "UPDATE EQUIPMENT SET "
            + "EquipmentTypeName = ?, "
            + "EquipmentType = ?, "
            + "AvailableQuantity = AvailableQuantity "
            + "+ (? - TotalQuantity), "
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
                System.out.println("Equipment updated.");
            } else {
                System.out.println("No equipment found with ID " + equipment.getEquipmentId() + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM EQUIPMENT WHERE EquipmentId = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Equipment deleted.");
            } else {
                System.out.println("No equipment found with ID " + id + ".");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                System.out.println("Cannot delete: this equipment is used in a reservation.");
            } else {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}