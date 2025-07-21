package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() {
        scanner.nextLine(); // consume leftover newline
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Security PIN (4-digit): ");
        int pin = scanner.nextInt();

        if (user_exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }

        long account_number = System.currentTimeMillis();
        double balance = 0.0;

        String register_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, full_name);
            preparedStatement.setString(3, email);
            preparedStatement.setDouble(4, balance);
            preparedStatement.setInt(5, pin);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successful!");
                System.out.println("Your Account Number is: " + account_number);
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean user_exist(String email) {
        String query = "SELECT * FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ User Login Method
    public String login() {
        scanner.nextLine(); // consume newline
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Security PIN: ");
        int pin = scanner.nextInt();

        String query = "SELECT * FROM accounts WHERE email = ? AND security_pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return email;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login failed
    }
}
