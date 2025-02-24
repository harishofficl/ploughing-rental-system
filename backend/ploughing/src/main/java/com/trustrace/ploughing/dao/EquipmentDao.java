package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EquipmentDao {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // Save or Update Equipment
    public Equipment save(Equipment equipment) {
        logger.info("Saving equipment with name: {}", equipment.getName());
        return mongoTemplate.save(equipment);
    }

    // Find All Equipments
    public List<Equipment> findAll() {
        logger.info("Fetching all equipments");
        return mongoTemplate.findAll(Equipment.class);
    }

    // Find Equipment by ID
    public Optional<Equipment> findById(String id) {
        logger.info("Fetching equipment by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Equipment.class));
    }

    // Find Equipments by Owner ID
    public List<Equipment> findByOwnerId(String ownerId) {
        logger.info("Fetching equipments for owner ID: {}", ownerId);
        Query query = new Query(Criteria.where("ownerId").is(ownerId));
        return mongoTemplate.find(query, Equipment.class);
    }

    // Delete Equipment by ID
    public void deleteById(String id) {
        logger.info("Deleting equipment by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Equipment.class);
    }
}
