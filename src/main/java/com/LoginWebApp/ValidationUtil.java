package com.LoginWebApp;

public class ValidationUtil {

    // Method to validate username and password (or other fields if needed)
    public static String validateLoginInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty.";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty.";
        }

        return null;
    }
    
    public static String validateUserDetails(String username, String firstName, String lastName, String password) {
    	if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty.";
        }

        if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
            return "Username must be at least 3 characters long and can only contain letters, numbers, dots, hyphens, and underscores.";
        }
    	
        if (firstName == null || firstName.trim().isEmpty() || !firstName.matches("^[a-zA-Z]+$")) {
            return "Invalid first name. Only letters are allowed.";
        }

        if (lastName == null || lastName.trim().isEmpty() || !lastName.matches("^[a-zA-Z]+$")) {
            return "Invalid last name. Only letters are allowed.";
        }

        if (password != null && !password.isEmpty()) {
            if (password.length() < 5) {
                return "Password must be at least 5 characters long.";
            }
        }

        return null;
    }
}