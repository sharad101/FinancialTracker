package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;


// Class: Transaction
// Description: Represents a financial transaction (Deposit or Payment)
// ------------------------------------
public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;


    // Constructor: Transaction
    // Description: Creates a new Transaction object with all fields.
    // ------------------------------------
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }


    // Getter Methods
    // Description: Return the values of each field (Date, Time, etc.)
    // ------------------------------------
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }


    // Setter Methods
    // Description: Allow updating the values of each field.
    // ------------------------------------
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    // Method: toFileString
    // Description: Converts the Transaction object into a single formatted line
    // ready to be saved into the transactions.csv file.
    // ------------------------------------
    public String toString() {
        return String.format("%-12s %-10s %-20s %-20s %10.2f", date.toString(), time.toString(), description, vendor, amount);
    }
}
