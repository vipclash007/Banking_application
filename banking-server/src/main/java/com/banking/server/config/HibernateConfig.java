package com.banking.server.config;

import com.banking.common.model.Account;
import com.banking.common.model.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfig {
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                
                // Load properties from hibernate.cfg.xml
                configuration.configure("hibernate.cfg.xml");
                
                // Add annotated classes
                configuration.addAnnotatedClass(Account.class);
                configuration.addAnnotatedClass(Transaction.class);
                
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
            }
        }
        return sessionFactory;
    }

    // Create programmatic configuration if hibernate.cfg.xml is not used
    public static SessionFactory getSessionFactoryProgrammatic() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                
                Properties settings = new Properties();
                settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/banking_db?createDatabaseIfNotExist=true");
                settings.put("hibernate.connection.username", "root");
                settings.put("hibernate.connection.password", "password");
                settings.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                settings.put("hibernate.show_sql", "true");
                settings.put("hibernate.format_sql", "true");
                settings.put("hibernate.hbm2ddl.auto", "update");
                
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Account.class);
                configuration.addAnnotatedClass(Transaction.class);
                
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
            }
        }
        return sessionFactory;
    }
}