package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.people.Owner;
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
public class OwnerDao {

    private static final Logger logger = LoggerFactory.getLogger(OwnerDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // Save or Update Owner
    public Owner save(Owner owner) {
        logger.info("Saving owner with email: {}", owner.getEmail());
        owner.setCreatedAt(LocalDateTime.now());
        if (owner.getRoles() == null || owner.getRoles().isEmpty()) {
            owner.setRoles(List.of("ROLE_OWNER"));
        }
        owner.setActive(true);
        return mongoTemplate.save(owner);
    }

    // Find All Owners
    public List<Owner> findAll() {
        logger.info("Fetching all owners");
        return mongoTemplate.findAll(Owner.class);
    }

    // Find Owner by ID
    public Optional<Owner> findById(String id) {
        logger.info("Fetching owner by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Owner.class));
    }

    // Find Owner by Email
    public Optional<Owner> findByEmail(String email) {
        logger.info("Fetching owner by email: {}", email);
        Query query = new Query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, Owner.class));
    }

    // Delete Owner by ID
    public void deleteById(String id) {
        logger.info("Deleting owner by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Owner.class);
    }
}
