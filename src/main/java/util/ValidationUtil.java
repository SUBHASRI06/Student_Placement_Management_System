package util;

import java.util.Scanner;

public class ValidationUtil {

    public static double readValidDouble(Scanner scanner, String prompt, double min, double max) {
        double val = -1;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                val = Double.parseDouble(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.println("ERROR: Please enter a value between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid input! Please enter a valid number.");
            }
        }
    }

    public static String readValidEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return email;
            }
            System.out.println("ERROR: Invalid email format! (e.g., student@example.com)");
        }
    }
}