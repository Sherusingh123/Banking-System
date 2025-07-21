package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/student";
    private static final String username = "root";
    private static final String password = "admin";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;

            while (true) {
                System.out.println("\n*** WELCOME TO BANKING SYSTEM ***\n");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = scanner.nextInt();

                switch (choice1) {
                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();  // ✅ Corrected line here
                        if (email != null) {
                            System.out.println("\nUser Logged In Successfully!");

                            if (!accounts.account_exist(email)) {
                                System.out.println("\nYou don't have a bank account yet.");
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit to main menu");
                                System.out.print("Enter your choice: ");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println("\n1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
                                choice2 = scanner.nextInt();

                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        System.out.println("Logged out successfully.");
                                        break;
                                    default:
                                        System.out.println("Invalid option. Please try again.");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Security PIN!");
                        }
                        break;

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;

                    default:
                        System.out.println("Enter a valid choice (1-3).");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection or query failed:");
            e.printStackTrace();
        }
    }
}
