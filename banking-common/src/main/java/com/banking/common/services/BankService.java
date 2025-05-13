package com.banking.common.services;

import com.banking.common.model.Account;
import com.banking.common.model.Transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BankService extends Remote {
    
    // Account operations
    Account createAccount(String accountHolderName, double initialBalance) throws RemoteException;
    
    Account getAccount(String accountNumber) throws RemoteException;
    
    double getBalance(String accountNumber) throws RemoteException;
    
    // Transaction operations
    boolean deposit(String accountNumber, double amount) throws RemoteException;
    
    boolean withdraw(String accountNumber, double amount) throws RemoteException;
    
    List<Transaction> getTransactionHistory(String accountNumber) throws RemoteException;
}
