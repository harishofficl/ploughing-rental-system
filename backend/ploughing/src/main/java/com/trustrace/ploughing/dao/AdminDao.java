package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.people.Admin;
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
public class AdminDao {

    private static final Logger logger = LoggerFactory.getLogger(AdminDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // Save or Update Admin
    public Admin save(Admin admin) {
        logger.info("Saving admin with email: {}", admin.getEmail());
        admin.setCreatedAt(LocalDateTime.now());
        if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
            admin.setRoles(List.of("ROLE_ADMIN"));
        }
        admin.setActive(true);
        return mongoTemplate.save(admin);
    }

    // Find All Admins
    public List<Admin> findAll() {
        logger.info("Fetching all admins");
        return mongoTemplate.findAll(Admin.class);
    }

    // Find Admin by ID
    public Optional<Admin> findById(String id) {
        logger.info("Fetching admin by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Admin.class));
    }

    // Find Admin by Email
    public Optional<Admin> findByEmail(String email) {
        logger.info("Fetching admin by email: {}", email);
        Query query = new Query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, Admin.class));
    }

    // Update Admin by ID
    public Admin updateById(String id, Admin admin) {
        logger.info("Updating admin by ID: {}", id);
        admin.setId(id);
        admin.setCreatedAt(findById(id).isPresent() ? findById(id).get().getCreatedAt() : LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        return mongoTemplate.save(admin);
    }

    // Delete Admin by ID
    public void deleteById(String id) {
        logger.info("Deleting admin by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Admin.class);
    }
}
