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
    public static void loadTransactions(String fileName) {
        File file = new File(fileName);

        try {
            if (!file.exists()) {
                System.out.println("File does not exist");
                return;
            }

            // Initialize BufferedReader to read file
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                //Parse the transactions.csv
                LocalDate date = LocalDate.parse(parts[0]); //
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
    // Description: Prompts user for payment info, saves to list and file.
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
    private static void saveTransaction(Transaction transaction) {
        try {

            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
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

            String input = scanner.nextLine();

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
    // Description: Displays all transactions
    private static void displayLedger() {
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }


    // Method: displayDeposits
    // Description: Displays only deposits
    private static void displayDeposits() {
        // This method displays only deposits of amount greater than zero
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction.toString());
            }
        }
    }


    // Method: displayPayments
    // Description: Displays only payments
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
    // Description: Displays report options like month-to-date, previous month, vendor search,
    // and the challenge which is the custom search
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
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine();

            //Today is the variable that stores the current date
            LocalDate today = LocalDate.now();

            switch (input) {
                case "1":
                    //The startDate parameter inside the method receives the value of today,
                    //creates a new date representing the first day of the current month
                    //The endDate parameter inside the method receives the value of today at the time of the call
                    filterTransactionsByDate(today.withDayOfMonth(1), today);
                    break;
                case "2":
                    //The function today.minusMonths(1) subtracts one month from today's date,
                    //prevMonthStart gives you the start of the month
                    //The function prevMonthStart.lengthofMonth gives you num of days in that month,
                    //Using withDayofMonth then returns the last day of the month
                    LocalDate prevMonthStart = today.minusMonths(1).withDayOfMonth(1);
                    LocalDate prevMonthEnd = prevMonthStart.withDayOfMonth(prevMonthStart.lengthOfMonth());
                    filterTransactionsByDate(prevMonthStart, prevMonthEnd);
                    break;
                case "3":
                    //The startDate parameter inside the method receives the value of today,
                    //creates a new date representing the first day of the current month
                    //The endDate parameter inside the method receives the value of today at the time of the call
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
                case "6":
                    customSearch(scanner);
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
    // Description: Filters and displays transactions between startDate and endDate.
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
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
    // Description: Filters and displays transactions matching the given vendor name.
    private static void filterTransactionsByVendor(String vendor) {
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

    // Challenge
    // Method: customSearch
    // Description: Filters and displays transactions given the custom input by user
    private static void customSearch(Scanner scanner){
        System.out.print("Start Date(yyyy-mm-dd): ");
        String startDatestring = scanner.nextLine();
        // If the user input is empty, set startDate to null.
        // Else convert the input string into a LocalDate using the DATE_FORMATTER.
        LocalDate startDate = startDatestring.isEmpty() ? null: LocalDate.parse(startDatestring, DATE_FORMATTER);

        System.out.print("End Date(yyyy-mm-dd): ");
        String endDatestring = scanner.nextLine();
        // If the user input is empty, set endDate to null.
        // Else convert the input string into a LocalDate using the DATE_FORMATTER.
        LocalDate endDate = endDatestring.isEmpty() ? null: LocalDate.parse(endDatestring, DATE_FORMATTER);

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        String amountString = scanner.nextLine();
        Double amount = amountString.isEmpty() ? null: Double.parseDouble(amountString);

        boolean found = false;
        System.out.printf("%-12s %-10s %-20s %-20s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            boolean match = true;

            // It checks whether the string startDatestring is empty using .isEmpty().
            // If it is empty, it sets startDate to null.
            // If it is not empty, it parses the string into a LocalDate using the specified DATE_FORMATTER.
            if (startDate != null && transaction.getDate().isBefore(startDate)) match = false;
            // It checks if endDate is not null and whether the transaction's date is after the given endDate.
            // If both conditions are true, it sets match to false, so this transaction does not fall within the valid range
            if (endDate != null && transaction.getDate().isAfter(endDate)) match = false;
            // If the user entered a description, check if the transaction's description contains it
            if (!description.isEmpty() && !transaction.getDescription().toLowerCase().contains(description.toLowerCase())) match = false;
            // Checks if the user entered a vendor name
            // If a vendor is provided, it compares the vendor input to the transaction list to the input
            if (!vendor.isEmpty() && !transaction.getVendor().toLowerCase().contains(vendor.toLowerCase())) match = false;
            // If an amount is provided, compares it to the transaction’s amount
            if (amount != null && transaction.getAmount() != amount) match = false;

            if (match) {
                System.out.println(transaction.toString());
                found = true;
            }

    if (!found){
        System.out.println("No transactions could be found based on your search ");
            }
        }
    }
}



