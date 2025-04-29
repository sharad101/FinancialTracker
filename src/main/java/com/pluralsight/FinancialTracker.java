package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    // List to hold all transactions in memory
    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);


    // Method: main
    // Description: Starts the program, shows Home Screen, handles user navigation.
    // ------------------------------------
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }


    // Method: loadTransactions
    // Description: Loads transactions from the CSV file into the ArrayList at program start.
    // ------------------------------------
    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        File file = new File(fileName);

        try {
            // Only proceed if the file exists
            if (!file.exists()) {
                System.out.println("File does not exist");
                return;
            }

            // Initialize BufferedReader to read file
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                //
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // Add valid transaction to the list
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading file");
        }
    }


    // Method: addDeposit
    // Description: Prompts user for deposit info and saves it to transactions list and CSV file.
    // ------------------------------------
    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList
        try {
            System.out.println("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

            System.out.println("Enter time (HH:mm:ss): ");
            LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

            System.out.println("Enter description: ");
            String description = scanner.nextLine();

            System.out.println("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.println("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            Transaction deposit = new Transaction(date, time, description, vendor, amount);
            transactions.add(deposit);
            saveTransaction(deposit);

            System.out.println("Deposit added successfully!");
        } catch (Exception e) {
            System.out.println("Invalid input. Try again.");
        }
    }


    // Method: addPayment
    // Description: Prompts user for payment info, makes amount negative, saves to list and file.
    // ------------------------------------
    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new pay
        try {
            System.out.println("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

            System.out.println("Enter time (HH:mm:ss): ");
            LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

            System.out.println("Enter description: ");
            String description = scanner.nextLine();

            System.out.println("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.println("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            // Turn it into a negative payment
            amount = -amount;

            Transaction payment = new Transaction(date, time, description, vendor, amount);
            transactions.add(payment);
            saveTransaction(payment);

            System.out.println("Payment added successfully!");
        } catch (Exception e) {
            System.out.println("Invalid input. Try again.");
        }
    }


    // Method: saveTransaction
    // Description: Saves a new transaction into the CSV file.
    // ------------------------------------
    private static void saveTransaction(Transaction transaction) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, true);
            writer.write(
                    transaction.getDate() + "|" +
                            transaction.getTime() + "|" +
                            transaction.getDescription() + "|" +
                            transaction.getVendor() + "|" +
                            transaction.getAmount() + "\n"
            );
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
    }


    // Method: ledgerMenu
    // Description: Displays the Ledger options to the user (All, Deposits, Payments, Reports, Home).
    // ------------------------------------
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    // Method: displayLedger
    // Description: Displays all transactions (most recent ones first).
    // ------------------------------------
    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }


    // Method: displayDeposits
    // Description: Displays only deposits (positive amounts).
    // ------------------------------------
    private static void displayDeposits() {
        // This method displays only deposits (amount > 0).
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction.toString());
            }
        }
    }


    // Method: displayPayments
    // Description: Displays only payments (negative amounts).
    // ------------------------------------
    private static void displayPayments() {
        // This method displays only payments (amount < 0).
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction.toString());
            }
        }
    }


    // Method: reportsMenu
    // Description: Displays report options like month-to-date, previous month, vendor search, etc.
    // ------------------------------------
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine();

            LocalDate today = LocalDate.now();

            switch (input) {
                case "1":
                    filterTransactionsByDate(today.withDayOfMonth(1), today);
                    break;
                case "2":
                    LocalDate prevMonthStart = today.minusMonths(1).withDayOfMonth(1);
                    LocalDate prevMonthEnd = prevMonthStart.withDayOfMonth(prevMonthStart.lengthOfMonth());
                    filterTransactionsByDate(prevMonthStart, prevMonthEnd);
                    break;
                case "3":
                    filterTransactionsByDate(today.withDayOfYear(1), today);
                    break;
                case "4":
                    LocalDate prevYearStart = today.minusYears(1).withDayOfYear(1);
                    LocalDate prevYearEnd = prevYearStart.withDayOfYear(prevYearStart.lengthOfYear());
                    filterTransactionsByDate(prevYearStart, prevYearEnd);
                    break;
                case "5":
                    System.out.print("Enter vendor name to search: ");
                    String vendor = scanner.nextLine();
                    filterTransactionsByVendor(vendor);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    // Method: filterTransactionsByDate
// Description: Filters and prints transactions between startDate and endDate.
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        boolean found = false;
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (!transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate)) {
                System.out.println(transaction.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found in the given date range.");
        }
    }

    // Method: filterTransactionsByVendor
// Description: Filters and prints transactions matching the given vendor name.
    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        boolean found = false;
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for vendor: " + vendor);
        }
    }
}



