package edu.addu.peers;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("   P.E.E.R.S. - Physical Exercise");
            System.out.println("   Equipment Reservation System");
            System.out.println("==========================================");
            System.out.println("1. Equipment Management (CRUD)");
            System.out.println("2. Reports & Queries (Week 4)");
            System.out.println("0. Exit Application");

            int choice = InputHelper.readInt(sc, "Enter your choice (0-2): ", 0, 2);

            switch (choice) {
                case 1:
                    handleEquipmentMenu(sc);
                    break;
                case 2:
                    handleReportsMenu(sc);
                    break;
                case 0:
                    System.out.println("\nThank you for using P.E.E.R.S. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Sub-menu 1: Equipment Management CRUD operations
     */
    private static void handleEquipmentMenu(Scanner sc) {
        while (true) {
            System.out.println("\n------------------------------------------");
            System.out.println("       EQUIPMENT MANAGEMENT (CRUD)");
            System.out.println("------------------------------------------");
            System.out.println("1. View All Equipment");
            System.out.println("2. Search Equipment by ID");
            System.out.println("3. Add New Equipment");
            System.out.println("4. Update Equipment");
            System.out.println("5. Delete Equipment");
            System.out.println("0. Back to Main Menu");

            int choice = InputHelper.readInt(sc, "Enter your choice (0-5): ", 0, 5);

            switch (choice) {
                case 1:
                    EquipmentDAO.viewAll();
                    break;
                case 2:
                    int id = InputHelper.readInt(sc, "Enter Equipment ID: ");
                    EquipmentDAO.searchById(id);
                    break;
                case 3:
                    String name = InputHelper.readString(sc, "Enter Equipment Name: ");
                    String type = InputHelper.readString(sc, "Enter Equipment Type (Sports/Fitness/Both): ");
                    int quantity = InputHelper.readInt(sc, "Enter Total Quantity: ");
                    EquipmentDAO.add(new Equipment(name, type, quantity));
                    break;
                case 4:
                    int updateId = InputHelper.readInt(sc, "Enter Equipment ID to Update: ");
                    String newName = InputHelper.readString(sc, "Enter New Equipment Name: ");
                    String newType = InputHelper.readString(sc, "Enter New Equipment Type (Sports/Fitness/Both): ");
                    int newQuantity = InputHelper.readInt(sc, "Enter New Total Quantity: ");
                    EquipmentDAO.update(new Equipment(updateId, newName, newType, newQuantity));
                    break;
                case 5:
                    int deleteId = InputHelper.readInt(sc, "Enter Equipment ID to Delete: ");
                    EquipmentDAO.delete(deleteId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Sub-menu 2: Information Retrieval / Week 4 Queries
     */
    private static void handleReportsMenu(Scanner sc) {
        while (true) {
            System.out.println("\n------------------------------------------");
            System.out.println("        REPORTS & QUERIES (WEEK 4)");
            System.out.println("------------------------------------------");
            System.out.println("1. Out-of-Stock Equipment (WHERE)");
            System.out.println("2. Scheduled Reservations with Details (JOIN)");
            System.out.println("3. Total Reservations per Reserver (COUNT + GROUP BY)");
            System.out.println("4. Equipment Never Reserved (Subquery)");
            System.out.println("5. Total Units Reserved per Category (SUM + GROUP BY)");
            System.out.println("6. Frequent Reservers (HAVING > 1)");
            System.out.println("7. Top 5 Most Reserved Equipment (ORDER BY + LIMIT)");
            System.out.println("8. Average Stock per Category (AVG + GROUP BY)");
            System.out.println("9. Run All 8 Queries (Full System Report)");
            System.out.println("0. Back to Main Menu");

            int choice = InputHelper.readInt(sc, "Enter your choice (0-9): ", 0, 9);

            switch (choice) {
                case 1:
                    QueryDAO.query1_OutOfStockEquipment();
                    break;
                case 2:
                    QueryDAO.query2_ScheduledReservations();
                    break;
                case 3:
                    QueryDAO.query3_ReservationsPerReserver();
                    break;
                case 4:
                    QueryDAO.query4_NeverReservedEquipment();
                    break;
                case 5:
                    QueryDAO.query5_TotalQuantityPerType();
                    break;
                case 6:
                    QueryDAO.query6_FrequentReservers();
                    break;
                case 7:
                    QueryDAO.query7_Top5ReservedEquipment();
                    break;
                case 8:
                    QueryDAO.query8_AverageStockPerCategory();
                    break;
                case 9:
                    runAllQueries();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Runs all 8 queries sequentially for quick demonstration and screenshot capture.
     */
    private static void runAllQueries() {
        System.out.println("\n========================================================");
        System.out.println("       RUNNING FULL WEEK 4 QUERY SUITE (Q1 - Q8)        ");
        System.out.println("========================================================");
        QueryDAO.query1_OutOfStockEquipment();
        QueryDAO.query2_ScheduledReservations();
        QueryDAO.query3_ReservationsPerReserver();
        QueryDAO.query4_NeverReservedEquipment();
        QueryDAO.query5_TotalQuantityPerType();
        QueryDAO.query6_FrequentReservers();
        QueryDAO.query7_Top5ReservedEquipment();
        QueryDAO.query8_AverageStockPerCategory();
        System.out.println("\n[All queries executed successfully.]");
    }
}
