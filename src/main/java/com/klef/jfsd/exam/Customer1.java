package com.klef.jfsd.exam;

import java.util.List;
import java.util.Scanner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class Customer1 {

    public static void main(String[] args) {
        Customer1 operations = new Customer1();
        operations.addCustomer();
        operations.applyCriteriaQueries();
    }

    public void addCustomer() {
        Scanner sc = new Scanner(System.in);
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sf = configuration.buildSessionFactory();
        Session session = sf.openSession();
        Transaction t = session.beginTransaction();
        Customer customer = new Customer();
        System.out.println("Would you like to enter a new customer? (y/n)");
        String userInput = sc.nextLine();

        if (userInput.equalsIgnoreCase("y")) {
            System.out.print("Enter Customer Name: ");
            customer.setName(sc.nextLine());
            System.out.print("Enter Customer Email: ");
            customer.setEmail(sc.nextLine());
            System.out.print("Enter Customer Age: ");
            customer.setAge(sc.nextInt());
            sc.nextLine();
            System.out.print("Enter Customer Location: ");
            customer.setLocation(sc.nextLine());
        } else {
            customer.setName("Default Name");
            customer.setEmail("default@example.com");
            customer.setAge(30);
            customer.setLocation("Default Location");
        }

        session.persist(customer);
        t.commit();
        System.out.println("Customer Added Successfully: " + customer);
        session.close();
        sf.close();
        sc.close();
    }

    public void applyCriteriaQueries() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sf = configuration.buildSessionFactory();
        Session session = sf.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);

        cq.select(root).where(cb.gt(root.get("age"), 25), cb.equal(root.get("location"), "New York"));
        Query<Customer> query = session.createQuery(cq);
        List<Customer> result = query.getResultList();

        System.out.println("\nCustomers with age > 25 and location 'New York':");
        for (Customer customer : result) {
            System.out.println(customer);
        }

        cq.select(root).where(cb.lt(root.get("age"), 30));
        query = session.createQuery(cq);
        result = query.getResultList();

        System.out.println("\nCustomers with age < 30:");
        for (Customer customer : result) {
            System.out.println(customer);
        }

        cq.select(root).where(cb.like(root.get("name"), "%John%"));
        query = session.createQuery(cq);
        result = query.getResultList();

        System.out.println("\nCustomers with 'John' in their name:");
        for (Customer customer : result) {
            System.out.println(customer);
        }

        cq.select(root).where(
            cb.between(root.get("age"), 20, 35),
            cb.like(root.get("name"), "%Jane%")
        );
        query = session.createQuery(cq);
        result = query.getResultList();

        System.out.println("\nCustomers between age 20 and 35 with name containing 'Jane':");
        for (Customer customer : result) {
            System.out.println(customer);
        }

        session.close();
        sf.close();
    }
}
