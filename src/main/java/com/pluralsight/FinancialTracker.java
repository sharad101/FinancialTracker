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
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                boolean validLine = true;
                for (String part : parts) {
                    if (part.trim().isEmpty()) {
                        validLine = false;
                        break;
                    }
                }

                if (validLine) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    transactions.add(transaction);
                }
            }

            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }


    // Method: addDeposit
    // Description: Prompts user for deposit info and saves it to transactions list and CSV file.
    // ------------------------------------
    private static void addDeposit(Scanner scanner) {
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

            amount = -amount; // Turn it into a negative payment

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
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
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
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
        }
    }


    // Method: displayDeposits
    // Description: Displays only deposits (positive amounts).
    // ------------------------------------
    private static void displayDeposits() {
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }


    // Method: displayPayments
    // Description: Displays only payments (negative amounts).
    // ------------------------------------
    private static void displayPayments() {
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
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

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("Month To Date report is not implemented yet.");
                    break;
                case "2":
                    System.out.println("Previous Month report is not implemented yet.");
                    break;
                case "3":
                    System.out.println("Year To Date report is not implemented yet.");
                    break;
                case "4":
                    System.out.println("Previous Year report is not implemented yet.");
                    break;
                case "5":
                    System.out.println("Vendor search is not implemented yet.");
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
}
