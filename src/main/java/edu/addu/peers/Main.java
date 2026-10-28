package edu.addu.peers;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1. View All Equipment");
            System.out.println("2. Search Equipment by ID");
            System.out.println("3. Add Equipment");
            System.out.println("4. Update Equipment");
            System.out.println("5. Delete Equipment");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    EquipmentDAO.viewAll();
                    break;
                case 2:
                    System.out.print("Enter Equipment ID: ");
                    int id = sc.nextInt();
                    EquipmentDAO.searchById(id);
                    break;
                case 3:
                    sc.nextLine();

                    System.out.print("Enter Equipment Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Equipment Type: ");
                    String type = sc.next();
                    System.out.print("Enter Equipment Quantity: ");
                    int quantity = sc.nextInt();
                    EquipmentDAO.add(new Equipment(name, type, quantity));
                    break;
                case 4:
                    System.out.print("Enter Equipment ID to Update: ");
                    int updateId = sc.nextInt();

                    sc.nextLine();

                    System.out.print("Enter New Equipment Name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter New Equipment Type: ");
                    String newType = sc.next();
                    System.out.print("Enter New Equipment Quantity: ");
                    int newQuantity = sc.nextInt();
                    EquipmentDAO.update(new Equipment(updateId, newName, newType, newQuantity));
                    break;
                case 5:
                    System.out.print("Enter Equipment ID to Delete: ");
                    int deleteId = sc.nextInt();
                    EquipmentDAO.delete(deleteId);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
     }
}
