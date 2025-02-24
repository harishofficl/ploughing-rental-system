package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.people.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // Save or Update Customer
    public Customer save(Customer customer) {
        logger.info("Saving customer with email: {}", customer.getEmail());
        customer.setCreatedAt(LocalDateTime.now());
        if (customer.getRoles() == null || customer.getRoles().isEmpty()) {
            customer.setRoles(List.of("ROLE_CUSTOMER"));
        }
        customer.setActive(true);
        return mongoTemplate.save(customer);
    }

    // Find All Customers
    public List<Customer> findAll() {
        logger.info("Fetching all customers");
        return mongoTemplate.findAll(Customer.class);
    }

    // Find Customer by ID
    public Optional<Customer> findById(String id) {
        logger.info("Fetching customer by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Customer.class));
    }

    // Find Customer by Email
    public Optional<Customer> findByEmail(String email) {
        logger.info("Fetching customer by email: {}", email);
        Query query = new Query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, Customer.class));
    }

    // Delete Customer by ID
    public void deleteById(String id) {
        logger.info("Deleting customer by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Customer.class);
    }
}
