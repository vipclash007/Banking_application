package com.banking.client;

import com.banking.common.model.Account;
import com.banking.common.model.Transaction;
import com.banking.common.services.BankService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class BankClient {
    private BankService bankService;
    private Scanner scanner;
    
    public BankClient() {
        scanner = new Scanner(System.in);
    }
    
    public boolean connect(String host) {
        try {
            // Set security manager if needed
            // if (System.getSecurityManager() == null) {
            //     System.setSecurityManager(new SecurityManager());
            // }
            
            // Locate registry and lookup the service
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            bankService = (BankService) registry.lookup("BankService");
            return true;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
    
    public void startClientApp() {
        if (bankService == null) {
            System.out.println("Not connected to bank service. Please connect first.");
            return;
        }
        
        boolean running = true;
        
        while (running) {
            System.out.println("\n===== BANKING APPLICATION =====");
            System.out.println("1. Create New Account");
            System.out.println("2. View Account Details");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. View Transaction History");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            try {
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        viewAccountDetails();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        viewTransactionHistory();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using the Banking Application!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void createAccount() throws Exception {
        System.out.println("\n----- CREATE NEW ACCOUNT -----");
        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (initialDeposit < 0) {
            System.out.println("Initial deposit cannot be negative.");
            return;
        }
        
        Account account = bankService.createAccount(name, initialDeposit);
        
        if (account != null) {
            System.out.println("\nAccount created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder: " + account.getAccountHolderName());
            System.out.println("Initial Balance: $" + account.getBalance());
        } else {
            System.out.println("Failed to create account. Please try again.");
        }
    }
    
    private void viewAccountDetails() throws Exception {
        System.out.println("\n----- VIEW ACCOUNT DETAILS -----");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bankService.getAccount(accountNumber);
        
        if (account != null) {
            System.out.println("\nAccount Details:");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder: " + account.getAccountHolderName());
            System.out.println("Current Balance: $" + account.getBalance());
        } else {
            System.out.println("Account not found. Please check the account number.");
        }
    }
    
    private void deposit() throws Exception {
        System.out.println("\n----- DEPOSIT MONEY -----");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bankService.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found. Please check the account number.");
            return;
        }
        
        System.out.print("Enter deposit amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }
        
        boolean success = bankService.deposit(accountNumber, amount);
        
        if (success) {
            double newBalance = bankService.getBalance(accountNumber);
            System.out.println("\nDeposit successful!");
            System.out.println("Deposited Amount: $" + amount);
            System.out.println("New Balance: $" + newBalance);
        } else {
            System.out.println("Deposit failed. Please try again.");
        }
    }
    
    private void withdraw() throws Exception {
        System.out.println("\n----- WITHDRAW MONEY -----");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bankService.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found. Please check the account number.");
            return;
        }
        
        System.out.println("Current Balance: $" + account.getBalance());
        System.out.print("Enter withdrawal amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be greater than zero.");
            return;
        }
        
        if (amount > account.getBalance()) {
            System.out.println("Insufficient funds. Your current balance is $" + account.getBalance());
            return;
        }
        
        boolean success = bankService.withdraw(accountNumber, amount);
        
        if (success) {
            double newBalance = bankService.getBalance(accountNumber);
            System.out.println("\nWithdrawal successful!");
            System.out.println("Withdrawn Amount: $" + amount);
            System.out.println("New Balance: $" + newBalance);
        } else {
            System.out.println("Withdrawal failed. Please try again.");
        }
    }
    
    private void viewTransactionHistory() throws Exception {
        System.out.println("\n----- TRANSACTION HISTORY -----");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bankService.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found. Please check the account number.");
            return;
        }
        
        List<Transaction> transactions = bankService.getTransactionHistory(accountNumber);
        
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }
        
        System.out.println("\nTransaction History for Account: " + accountNumber);
        System.out.println("------------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-10s | %-20s\n", "ID", "Type", "Amount", "Date");
        System.out.println("------------------------------------------------------");
        
        for (Transaction t : transactions) {
            System.out.printf("%-5d | %-12s | $%-9.2f | %-20s\n", 
                    t.getId(), t.getType(), t.getAmount(), t.getTransactionDate());
        }
    }
    
    public static void main(String[] args) {
        BankClient client = new BankClient();
        
        System.out.println("=== Banking Client Application ===");
        System.out.print("Enter server hostname/IP (default: localhost): ");
        Scanner scanner = new Scanner(System.in);
        String host = scanner.nextLine().trim();
        
        if (host.isEmpty()) {
            host = "localhost";
        }
        
        System.out.println("Connecting to " + host + "...");
        
        if (client.connect(host)) {
            System.out.println("Connected to Bank Server successfully!");
            client.startClientApp();
        } else {
            System.out.println("Failed to connect to Bank Server. Please make sure the server is running.");
        }
        
        scanner.close();
    }
}
