package com.LoginWebApp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseValidator {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    static Properties properties = new Properties();

    static {
        try {
            // Load config.properties from resources
            InputStream input = DatabaseValidator.class.getClassLoader().getResourceAsStream("config.properties");

            if (input != null) {
                properties.load(input);
            } else {
                System.out.println("Sorry, unable to find config.properties");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to add a new user to the database
    public static boolean addNewUser(String username, String password, String fullName) {
        try {
        	String environment = System.getenv("ENVIRONMENT"); // Set ENVIRONMENT as local or aws in your environment variables
            
        	System.out.println("Environment: " + System.getenv("ENVIRONMENT"));
        	
        	String dbUrl = properties.getProperty(environment + ".db.url");
            String dbUsername = properties.getProperty(environment + ".db.username");
            String dbPassword = properties.getProperty(environment + ".db.password");
            
            System.out.println("About to connect in add new user.");

            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            
            System.out.println("Just connected in add new user.");

            String query = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);  // Hashed password
            statement.setString(3, fullName);

            int rowsInserted = statement.executeUpdate();
            
            System.out.println("Rows inserted: " + rowsInserted);
            // Clean up
            statement.close();
            connection.close();

            return rowsInserted > 0;  // Return true if the insertion was successful
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserByUsername(String username) {
        User user = null;

        try {
        	String environment = System.getenv("ENVIRONMENT"); // Set ENVIRONMENT as local or aws in your environment variables
            
        	System.out.println("Environment: " + System.getenv("ENVIRONMENT"));
        	
        	String dbUrl = properties.getProperty(environment + ".db.url");
            String dbUsername = properties.getProperty(environment + ".db.username");
            String dbPassword = properties.getProperty(environment + ".db.password");
            
            System.out.println("Environment URL: " + environment + ".db.url");
            
            // Establishing the database connection
            System.out.println("Attempting to connect to DB with URL: " + dbUrl);
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("Connected successfully!");

            // SQL query to fetch user details
            String query = "SELECT username, password, full_name FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // Execute query and retrieve result
            ResultSet resultSet = statement.executeQuery();
            
            // If user is found, create a new User object and return it
            if (resultSet.next()) {
                String retrievedUsername = resultSet.getString("username");
                String retrievedPassword = resultSet.getString("password");
                String fullName = resultSet.getString("full_name");

                user = new User(retrievedUsername, retrievedPassword, fullName);
            }

            // Closing resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}