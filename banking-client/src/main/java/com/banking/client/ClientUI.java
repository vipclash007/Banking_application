package com.banking.client;

import com.banking.common.model.Account;
import com.banking.common.model.Transaction;
import com.banking.common.services.BankService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientUI extends JFrame {
    private BankService bankService;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Login panel components
    private JPanel loginPanel;
    private JTextField serverField;
    private JButton connectButton;
    
    // Dashboard panel components
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane;
    
    // Account panel components
    private JPanel accountPanel;
    private JTextField accountNumberField;
    private JTextField holderNameField;
    private JTextField initialBalanceField;
    private JButton createAccountButton;
    private JButton viewAccountButton;
    
    // Transaction panel components
    private JPanel transactionPanel;
    private JTextField transAccNumberField;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawButton;
    
    // History panel components
    private JPanel historyPanel;
    private JTextField historyAccNumberField;
    private JButton viewHistoryButton;
    private JTable transactionTable;
    private JScrollPane scrollPane;
    
    public ClientUI() {
        setTitle("Banking Client Application");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize main panel with card layout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create panels
        createLoginPanel();
        createDashboardPanel();
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(dashboardPanel, "dashboard");
        
        // Show login panel first
        cardLayout.show(mainPanel, "login");
        
        add(mainPanel);
        setLocationRelativeTo(null);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Connect to Bank Server", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel serverLabel = new JLabel("Server Host:");
        serverField = new JTextField("localhost", 20);
        connectButton = new JButton("Connect");
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(serverLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(serverField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(connectButton, gbc);
        
        // Add action listener to connect button
        connectButton.addActionListener(e -> {
            try {
                String host = serverField.getText().trim();
                if (host.isEmpty()) {
                    host = "localhost";
                }
                
                boolean connected = connectToServer(host);
                if (connected) {
                    JOptionPane.showMessageDialog(this, "Connected to Bank Server successfully!");
                    cardLayout.show(mainPanel, "dashboard");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to connect to server.",
                            "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Add components to login panel
        loginPanel.add(titleLabel, BorderLayout.NORTH);
        loginPanel.add(formPanel, BorderLayout.CENTER);
    }
    
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create tabs
        createAccountTab();
        createTransactionTab();
        createHistoryTab();
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Account", accountPanel);
        tabbedPane.addTab("Transaction", transactionPanel);
        tabbedPane.addTab("History", historyPanel);
        
        // Add tabbed pane to dashboard
        dashboardPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createAccountTab() {
        accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Components for creating an account
        JLabel createLabel = new JLabel("Create New Account", JLabel.LEFT);
        createLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel holderLabel = new JLabel("Account Holder Name:");
        holderNameField = new JTextField(20);
        
        JLabel balanceLabel = new JLabel("Initial Balance:");
        initialBalanceField = new JTextField(20);
        
        createAccountButton = new JButton("Create Account");
        
        // Components for viewing an account
        JLabel viewLabel = new JLabel("View Account Details", JLabel.LEFT);
        viewLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel accNumberLabel = new JLabel("Account Number:");
        accountNumberField = new JTextField(20);
        
        viewAccountButton = new JButton("View Details");
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(createLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(holderLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(holderNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(balanceLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(initialBalanceField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(createAccountButton, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(new JSeparator(), gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(viewLabel, gbc);
        
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        formPanel.add(accNumberLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(accountNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(viewAccountButton, gbc);
        
        // Add action listeners
        createAccountButton.addActionListener(e -> {
            try {
                String holderName = holderNameField.getText().trim();
                if (holderName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter account holder name");
                    return;
                }
                
                double initialBalance;
                try {
                    initialBalance = Double.parseDouble(initialBalanceField.getText().trim());
                    if (initialBalance < 0) {
                        JOptionPane.showMessageDialog(this, "Initial balance cannot be negative");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid amount");
                    return;
                }
                
                Account account = bankService.createAccount(holderName, initialBalance);
                if (account != null) {
                    JOptionPane.showMessageDialog(this,
                            "Account created successfully!\n" +
                                    "Account Number: " + account.getAccountNumber() + "\n" +
                                    "Account Holder: " + account.getAccountHolderName() + "\n" +
                                    "Initial Balance: $" + account.getBalance());
                    holderNameField.setText("");
                    initialBalanceField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        viewAccountButton.addActionListener(e -> {
            try {
                String accountNumber = accountNumberField.getText().trim();
                if (accountNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter account number");
                    return;
                }
                
                Account account = bankService.getAccount(accountNumber);
                if (account != null) {
                    JOptionPane.showMessageDialog(this,
                            "Account Details:\n" +
                                    "Account Number: " + account.getAccountNumber() + "\n" +
                                    "Account Holder: " + account.getAccountHolderName() + "\n" +
                                    "Current Balance: $" + account.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        // Add form panel to account panel
        accountPanel.add(formPanel, BorderLayout.NORTH);
    }
    
    private void createTransactionTab() {
        transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel transactionLabel = new JLabel("Make a Transaction", JLabel.LEFT);
        transactionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel accNumberLabel = new JLabel("Account Number:");
        transAccNumberField = new JTextField(20);
        
        JLabel amountLabel = new JLabel("Amount ($):");
        amountField = new JTextField(20);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(transactionLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(accNumberLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(transAccNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(amountLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Add action listeners
        depositButton.addActionListener(e -> {
            try {
                String accountNumber = transAccNumberField.getText().trim();
                if (accountNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter account number");
                    return;
                }
                
                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText().trim());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Amount must be greater than zero");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid amount");
                    return;
                }
                
                boolean success = bankService.deposit(accountNumber, amount);
                if (success) {
                    double newBalance = bankService.getBalance(accountNumber);
                    JOptionPane.showMessageDialog(this,
                            "Deposit successful!\n" +
                                    "Deposited Amount: $" + amount + "\n" +
                                    "New Balance: $" + newBalance);
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed. Please check account number.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        withdrawButton.addActionListener(e -> {
            try {
                String accountNumber = transAccNumberField.getText().trim();
                if (accountNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter account number");
                    return;
                }
                
                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText().trim());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Amount must be greater than zero");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid amount");
                    return;
                }
                
                double currentBalance = bankService.getBalance(accountNumber);
                if (currentBalance < 0) {
                    JOptionPane.showMessageDialog(this, "Account not found");
                    return;
                }
                
                if (amount > currentBalance) {
                    JOptionPane.showMessageDialog(this,
                            "Insufficient funds. Current balance: $" + currentBalance);
                    return;
                }
                
                boolean success = bankService.withdraw(accountNumber, amount);
                if (success) {
                    double newBalance = bankService.getBalance(accountNumber);
                    JOptionPane.showMessageDialog(this,
                            "Withdrawal successful!\n" +
                                    "Withdrawn Amount: $" + amount + "\n" +
                                    "New Balance: $" + newBalance);
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        // Add form panel to transaction panel
        transactionPanel.add(formPanel, BorderLayout.NORTH);
    }
    
    private void createHistoryTab() {
        historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel accNumberLabel = new JLabel("Account Number:");
        historyAccNumberField = new JTextField(15);
        viewHistoryButton = new JButton("View History");
        topPanel.add(accNumberLabel);
        topPanel.add(historyAccNumberField);
        topPanel.add(viewHistoryButton);
        
        // Create table for transaction history
        String[] columnNames = {"ID", "Type", "Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(model);
        scrollPane = new JScrollPane(transactionTable);
        
        // Add action listener to view history button
        viewHistoryButton.addActionListener(e -> {
            try {
                String accountNumber = historyAccNumberField.getText().trim();
                if (accountNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter account number");
                    return;
                }
                
                List<Transaction> transactions = bankService.getTransactionHistory(accountNumber);
                
                DefaultTableModel tableModel = (DefaultTableModel) transactionTable.getModel();
                tableModel.setRowCount(0); // Clear existing data
                
                if (transactions == null || transactions.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No transactions found for this account");
                } else {
                    for (Transaction t : transactions) {
                        Object[] row = {
                                t.getId(),
                                t.getType(),
                                String.format("$%.2f", t.getAmount()),
                                t.getTransactionDate()
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        // Add components to history panel
        historyPanel.add(topPanel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private boolean connectToServer(String host) {
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
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientUI().setVisible(true);
        });
    }
}