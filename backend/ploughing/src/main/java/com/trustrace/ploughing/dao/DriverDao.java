package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.RentalRecord;
import com.trustrace.ploughing.model.people.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        driver.setActive(true);
        Driver createdDriver = mongoTemplate.save(driver);
        ownerDao.addDriverId(createdDriver.getOwnerId(), createdDriver.getId());
        return createdDriver;
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

    public Page<Driver> getDriversByOwnerIdPaginatedContainingName(String ownerId, int page, int size, String search) {
        Criteria criteria = Criteria.where("ownerId").is(ownerId);
        if (search != null && !search.isEmpty()) {
            criteria.and("name").regex(search, "i");
        }
        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Driver.class);
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);
        List<Driver> drivers = mongoTemplate.find(query, Driver.class);
        return new PageImpl<>(drivers, pageable, total);
    }
}
