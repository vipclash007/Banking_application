package com.banking.server.dao;

import com.banking.common.model.Account;
import com.banking.common.model.Transaction;
import com.banking.server.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
// Removed duplicate import of org.hibernate.Transaction
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class AccountDAO {
    private final SessionFactory sessionFactory;
    
    public AccountDAO() {
        this.sessionFactory = HibernateConfig.getSessionFactory();
    }
    
    public Account createAccount(String accountHolderName, double initialBalance) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        Account account = null;
        
        try {
            tx = session.beginTransaction();
            
            // Generate unique account number
            String accountNumber = "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Create and save the account
            account = new Account(accountNumber, accountHolderName, initialBalance);
            session.persist(account);
            
            // Create initial deposit transaction if balance is greater than 0
            if (initialBalance > 0) {
                Transaction transaction = new Transaction(account, initialBalance, Transaction.TransactionType.DEPOSIT);
                session.persist(transaction);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return account;
    }
    
    public Account getAccount(String accountNumber) {
        Session session = sessionFactory.openSession();
        Account account = null;
        
        try {
            Query<Account> query = session.createQuery("FROM Account WHERE accountNumber = :accNum", Account.class);
            query.setParameter("accNum", accountNumber);
            account = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return account;
    }
    
    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        boolean success = false;
        
        try {
            tx = session.beginTransaction();
            
            Account account = getAccount(accountNumber);
            if (account == null) {
                return false;
            }
            
            // Update balance
            account.setBalance(account.getBalance() + amount);
            session.merge(account);
            
            // Create transaction record
            Transaction transaction = new Transaction(account, amount, Transaction.TransactionType.DEPOSIT);
            session.persist(transaction);
            
            tx.commit();
            success = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return success;
    }
    
    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        boolean success = false;
        
        try {
            tx = session.beginTransaction();
            
            Account account = getAccount(accountNumber);
            if (account == null || account.getBalance() < amount) {
                return false;
            }
            
            // Update balance
            account.setBalance(account.getBalance() - amount);
            session.merge(account);
            
            // Create transaction record
            Transaction transaction = new Transaction(account, amount, Transaction.TransactionType.WITHDRAWAL);
            session.persist(transaction);
            
            tx.commit();
            success = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return success;
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Session session = sessionFactory.openSession();
        List<Transaction> transactions = null;
        
        try {
            Account account = getAccount(accountNumber);
            if (account != null) {
                Query<Transaction> query = session.createQuery(
                        "FROM Transaction WHERE account.id = :accountId ORDER BY transactionDate DESC", 
                        Transaction.class);
                query.setParameter("accountId", account.getId());
                transactions = query.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return transactions;
    }
}
