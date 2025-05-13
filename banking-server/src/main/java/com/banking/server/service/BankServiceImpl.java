package com.banking.server.service;

import com.banking.common.model.Account;
import com.banking.common.model.Transaction;
import com.banking.common.services.BankService;
import com.banking.server.dao.AccountDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class BankServiceImpl extends UnicastRemoteObject implements BankService {
    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO;
    
    public BankServiceImpl() throws RemoteException {
        super();
        this.accountDAO = new AccountDAO();
    }
    
    @Override
    public Account createAccount(String accountHolderName, double initialBalance) throws RemoteException {
        return accountDAO.createAccount(accountHolderName, initialBalance);
    }
    
    @Override
    public Account getAccount(String accountNumber) throws RemoteException {
        return accountDAO.getAccount(accountNumber);
    }
    
    @Override
    public double getBalance(String accountNumber) throws RemoteException {
        Account account = accountDAO.getAccount(accountNumber);
        return account != null ? account.getBalance() : -1;
    }
    
    @Override
    public boolean deposit(String accountNumber, double amount) throws RemoteException {
        return accountDAO.deposit(accountNumber, amount);
    }
    
    @Override
    public boolean withdraw(String accountNumber, double amount) throws RemoteException {
        return accountDAO.withdraw(accountNumber, amount);
    }
    
    @Override
    public List<Transaction> getTransactionHistory(String accountNumber) throws RemoteException {
        return accountDAO.getTransactionHistory(accountNumber);
    }
}