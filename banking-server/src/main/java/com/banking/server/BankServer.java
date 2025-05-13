package com.banking.server;

import com.banking.common.services.BankService;
import com.banking.server.service.BankServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BankServer {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Bank Server...");
            
            // // Set security manager if needed
            // if (System.getSecurityManager() == null) {
            //     System.setSecurityManager(new SecurityManager());
            // }
            
            // Create and export the remote service
            BankService bankService = new BankServiceImpl();
            
            // Create or locate the registry
            Registry registry = null;
            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("RMI Registry created at port 1099");
            } catch (Exception e) {
                System.out.println("RMI Registry already exists, getting reference...");
                registry = LocateRegistry.getRegistry(1099);
            }
            
            // Bind the remote object to the registry
            registry.rebind("BankService", bankService);
            
            System.out.println("Bank Server is ready to serve clients!");
        } catch (Exception e) {
            System.err.println("Bank Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}