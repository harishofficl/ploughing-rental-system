package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Equipment;
import com.trustrace.ploughing.model.RentalRecord;
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

import java.util.List;
import java.util.Optional;

@Repository
public class EquipmentDao {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OwnerDao ownerDao;

    // Save Equipment
    public Equipment save(Equipment equipment) {
        logger.info("Saving equipment with name: {}", equipment.getName());
        Equipment createdEquipment = mongoTemplate.save(equipment);
        ownerDao.addEquipmentId(createdEquipment.getOwnerId(), createdEquipment.getId());
        return createdEquipment;
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

    //Update Equipment by ID
    public Equipment updateById(String id, Equipment equipment) {
        logger.info("Updating equipment by ID: {}", id);
        equipment.setId(id);
        return mongoTemplate.save(equipment);
    }

    //Update price of Equipment
    public Equipment updatePrice(String id, int price) {
        logger.info("Updating price of equipment by ID: {}", id);
        Equipment equipment = mongoTemplate.findById(id, Equipment.class);
        if(equipment != null) {
            equipment.setPrice(price);
            return mongoTemplate.save(equipment);
        }
        return null;
    }

    // Delete Equipment by ID
    public void deleteById(String id) {
        logger.info("Deleting equipment by ID: {}", id);
        if(findById(id).isEmpty()) {
            logger.info("Equipment with ID: {} not found", id);
            return;
        }
        Query query = new Query(Criteria.where("id").is(id));
        ownerDao.removeEquipmentId(findById(id).get().getOwnerId(), id);
        mongoTemplate.remove(query, Equipment.class);
    }

    public Page<Equipment> getEquipmentsByOwnerIdPaginatedContainingName(String ownerId, int page, int size, String search) {
        Criteria criteria = Criteria.where("ownerId").is(ownerId);
        if (search != null && !search.isEmpty()) {
            criteria.and("name").regex(search, "i");
        }
        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Equipment.class);
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);
        List<Equipment> equipments = mongoTemplate.find(query, Equipment.class);
        return new PageImpl<>(equipments, pageable, total);
    }
}
