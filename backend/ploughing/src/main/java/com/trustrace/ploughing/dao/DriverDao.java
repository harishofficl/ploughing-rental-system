package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.people.Driver;
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
public class DriverDao {

    private static final Logger logger = LoggerFactory.getLogger(DriverDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OwnerDao ownerDao;

    // Save Driver
    public Driver save(Driver driver) {
        logger.info("Saving driver with name: {}", driver.getName());
        driver.setCreatedAt(LocalDateTime.now());
        if (driver.getRoles() == null || driver.getRoles().isEmpty()) {
            driver.setRoles(List.of("ROLE_DRIVER"));
        }
        ownerDao.addDriverId(driver.getOwnerId(), driver.getId());
        driver.setActive(true);
        return mongoTemplate.save(driver);
    }

    // Find All Drivers
    public List<Driver> findAll() {
        logger.info("Fetching all drivers");
        return mongoTemplate.findAll(Driver.class);
    }

    // Find Driver by ID
    public Optional<Driver> findById(String id) {
        logger.info("Fetching driver by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Driver.class));
    }

    // Find Drivers by Owner ID
    public List<Driver> findByOwnerId(String ownerId) {
        logger.info("Fetching drivers for owner ID: {}", ownerId);
        Query query = new Query(Criteria.where("ownerId").is(ownerId));
        return mongoTemplate.find(query, Driver.class);
    }

    // Update Driver by ID
    public Driver updateById(String id, Driver driver) {
        logger.info("Updating driver by ID: {}", id);
        driver.setId(id);
        driver.setCreatedAt(findById(id).isPresent() ? findById(id).get().getCreatedAt() : LocalDateTime.now());
        driver.setUpdatedAt(LocalDateTime.now());
        return mongoTemplate.save(driver);
    }

    // Delete Driver by ID
    public void deleteById(String id) {
        logger.info("Deleting driver by ID: {}", id);
        if (findById(id).isEmpty()) {
            logger.info("Driver with ID: {} not found", id);
            return;
        }
        Query query = new Query(Criteria.where("id").is(id));
        ownerDao.removeDriverId(findById(id).get().getOwnerId(), id);
        mongoTemplate.remove(query, Driver.class);
    }
}
