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
}
