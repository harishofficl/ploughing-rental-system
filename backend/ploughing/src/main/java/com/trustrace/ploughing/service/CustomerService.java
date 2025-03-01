package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.CustomerDao;
import com.trustrace.ploughing.model.people.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    // Create or update a customer
    public Customer createCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    // Get a customer by ID
    public Optional<Customer> getCustomerById(String id) {
        return customerDao.findById(id);
    }

    // Get a customer by email
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerDao.findByEmail(email);
    }

    // Update a customer by ID
    public Customer updateCustomer(String id, Customer customer) {
        return customerDao.updateById(id, customer);
    }

    // Delete a customer by ID
    public boolean deleteCustomer(String id) {
        Optional<Customer> existingCustomer = customerDao.findById(id);
        if (existingCustomer.isPresent()) {
            customerDao.deleteById(id);
            return true;
        }
        return false;
    }

    // Get customers by ownerId and contains name
    public List<Customer> getCustomersByOwnerIdContainsName(String ownerId, String term) {
        return customerDao.findByOwnerIdAndContainsName(ownerId, term);
    }

    // get customer count by ownerId
    public long getCustomerCountByOwnerId(String ownerId) {
        return customerDao.countByOwnerId(ownerId);
    }
}
