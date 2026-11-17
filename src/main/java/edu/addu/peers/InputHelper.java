package edu.addu.peers;

import java.util.Scanner;

public class InputHelper {

    /**
     * Safely reads an integer from the user.
     * Keeps asking until a valid whole number is entered.
     */
    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // Safely reads an integer within a specific [min, max] range.
    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int val = readInt(sc, prompt);
            if (val >= min && val <= max) {
                return val;
            }
            System.out.printf("Please enter a choice between %d and %d.%n", min, max);
        }
    }

    // Reads an optional integer (press Enter to keep defaultVal).
    public static int readOptionalInt(Scanner sc, String prompt, int defaultVal) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return defaultVal;
            }
            try {
                int val = Integer.parseInt(line);
                if (val >= 0) {
                    return val;
                }
                System.out.println("Quantity cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // Reads a non-empty string from the user.
    public static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }

    // Reads an optional string (press Enter to keep defaultValue).
    public static String readOptionalString(Scanner sc, String prompt, String defaultValue) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) {
            return defaultValue;
        }
        return line;
    }

    // Validates Equipment Type against ENUM('Sports', 'Fitness', 'Both') case-insensitively.
    public static String readEquipmentType(Scanner sc, String prompt, String defaultType) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty() && defaultType != null) {
                return defaultType;
            }
            if (line.equalsIgnoreCase("Sports")) {
                return "Sports";
            }
            if (line.equalsIgnoreCase("Fitness")) {
                return "Fitness";
            }
            if (line.equalsIgnoreCase("Both")) {
                return "Both";
            }
            System.out.println("Invalid type. Please enter 'Sports', 'Fitness', or 'Both'.");
        }
    }
}
