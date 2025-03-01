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
import java.util.Objects;
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

    // Update Customer by ID
    public Customer updateById(String id, Customer customer) {
        logger.info("Updating customer by ID: {}", id);
        customer.setId(id);
        customer.setCreatedAt(findById(id).isPresent() ? findById(id).get().getCreatedAt() : LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return mongoTemplate.save(customer);
    }

    // Delete Customer by ID
    public void deleteById(String id) {
        logger.info("Deleting customer by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Customer.class);
    }

    // find by owner id and name that contains term
    public List<Customer> findByOwnerIdAndContainsName(String ownerId, String term) {
        logger.info("Fetching customers for Owner ID: {} and Name contains: {}", ownerId, term);
        Query query = new Query(Criteria.where("ownerId").is(ownerId).and("name").regex(term, "i"));
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        if(customers.isEmpty()) {
            logger.info("No customers found for Owner ID: {} and Name contains: {}", ownerId, term);
            return List.of();
        }
        return customers;
    }

    // total customers count by owner id
    public long countByOwnerId(String ownerId) {
        logger.info("Fetching total customers count for Owner ID: {}", ownerId);
        Query query = new Query(Criteria.where("ownerId").is(ownerId));
        return mongoTemplate.count(query, Customer.class);
    }
}
