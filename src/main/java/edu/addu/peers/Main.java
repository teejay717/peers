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
            System.out.println("3. Reservation Management (Reserve & Return)");
            System.out.println("0. Exit Application");

            int choice = InputHelper.readInt(sc, "Enter your choice (0-3): ", 0, 3);

            switch (choice) {
                case 1:
                    handleEquipmentMenu(sc);
                    break;
                case 2:
                    handleReportsMenu(sc);
                    break;
                case 3:
                    handleReservationMenu(sc);
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
                    String type = InputHelper.readEquipmentType(sc, "Enter Equipment Type (Sports/Fitness/Both): ", null);
                    int quantity = InputHelper.readInt(sc, "Enter Total Quantity: ");
                    EquipmentDAO.add(new Equipment(name, type, quantity));
                    break;
                case 4:
                    int updateId = InputHelper.readInt(sc, "Enter Equipment ID to Update: ");
                    Equipment existing = EquipmentDAO.getById(updateId);
                    if (existing == null) {
                        System.out.println("No equipment found with ID: " + updateId);
                        break;
                    }
                    System.out.printf("Found: %s | %s | Available: %d / Total: %d%n",
                            existing.getEquipmentTypeName(),
                            existing.getEquipmentType(),
                            existing.getAvailableQuantity(),
                            existing.getTotalQuantity());

                    String newName = InputHelper.readOptionalString(sc,
                            "Enter New Equipment Name [Press Enter to keep '" + existing.getEquipmentTypeName() + "']: ",
                            existing.getEquipmentTypeName());
                    String newType = InputHelper.readEquipmentType(sc,
                            "Enter New Equipment Type (Sports/Fitness/Both) [Press Enter to keep '" + existing.getEquipmentType() + "']: ",
                            existing.getEquipmentType());
                    int newQuantity = InputHelper.readOptionalInt(sc,
                            "Enter New Total Quantity [Press Enter to keep " + existing.getTotalQuantity() + "]: ",
                            existing.getTotalQuantity());
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

    /**
     * Sub-menu 3: Reservation Management (Reserve & Return)
     */
    private static void handleReservationMenu(Scanner sc) {
        while (true) {
            System.out.println("\n------------------------------------------");
            System.out.println("    RESERVATION MANAGEMENT (P.E.E.R.S.)   ");
            System.out.println("------------------------------------------");
            System.out.println("1. View All Reservations");
            System.out.println("2. Create New Reservation (Borrow Equipment)");
            System.out.println("3. Return Equipment (Mark as Completed)");
            System.out.println("0. Back to Main Menu");

            int choice = InputHelper.readInt(sc, "Enter your choice (0-3): ", 0, 3);

            switch (choice) {
                case 1:
                    ReservationDAO.viewAll();
                    break;
                case 2:
                    System.out.println("\n--- Current Equipment Inventory ---");
                    EquipmentDAO.viewAll();

                    int reserverId = InputHelper.readInt(sc, "\nEnter Reserver ID (e.g. 2 for Student, 1 for Prof): ");
                    String[] reserverInfo = ReservationDAO.getReserverInfo(reserverId);
                    if (reserverInfo == null) {
                        System.out.println("Error: Reserver ID " + reserverId + " does not exist.");
                        break;
                    }
                    String reserverName = reserverInfo[0];
                    String reserverType = reserverInfo[1];
                    System.out.printf("Reserver: %s (%s)%n", reserverName, reserverType);

                    String today = java.time.LocalDate.now().toString();
                    String date = InputHelper.readOptionalString(sc, "Enter Reservation Date (YYYY-MM-DD) [Press Enter for today " + today + "]: ", today);
                    String start = InputHelper.readOptionalString(sc, "Enter Start Time (HH:MM:SS) [Press Enter for '09:00:00']: ", "09:00:00");
                    String end = InputHelper.readOptionalString(sc, "Enter End Time (HH:MM:SS) [Press Enter for '10:00:00']: ", "10:00:00");

                    java.util.List<ReservationDAO.ItemRequest> items = new java.util.ArrayList<>();
                    while (true) {
                        int equipmentId = InputHelper.readInt(sc, "Enter Equipment ID to Reserve: ");
                        int quantity = InputHelper.readInt(sc, "Enter Quantity to Reserve: ");
                        items.add(new ReservationDAO.ItemRequest(equipmentId, quantity));

                        if ("Professor".equalsIgnoreCase(reserverType)) {
                            System.out.print("Add another equipment item to this class reservation? (y/n): ");
                            String more = sc.nextLine().trim();
                            if (!more.equalsIgnoreCase("y") && !more.equalsIgnoreCase("yes")) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }

                    ReservationDAO.createReservation(reserverId, items, date, start, end);
                    break;
                case 3:
                    System.out.println("\n--- Active Borrowed Reservations ---");
                    ReservationDAO.viewActive();

                    int resId = InputHelper.readInt(sc, "\nEnter Reservation ID to Return: ");
                    ReservationDAO.returnEquipment(resId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
