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

    // Save Owner
    public Owner save(Owner owner) {
        logger.info("Saving owner with email: {}", owner.getEmail());
        owner.setCreatedAt(LocalDateTime.now());
        if (owner.getRoles() == null || owner.getRoles().isEmpty()) {
            owner.setRoles(List.of("ROLE_OWNER"));
        }
        owner.setActive(true);
        owner.setVehicleIds(List.of());
        owner.setDriverIds(List.of());
        owner.setEquipmentIds(List.of());
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

    // Update Owner by ID
    public Owner updateById(String id, Owner owner) {
        logger.info("Updating owner by ID: {}", id);
        owner.setId(id);
        owner.setCreatedAt(findById(id).isPresent() ? findById(id).get().getCreatedAt() : LocalDateTime.now());
        owner.setUpdatedAt(LocalDateTime.now());
        return mongoTemplate.save(owner);
    }

    // Delete Owner by ID
    public void deleteById(String id) {
        logger.info("Deleting owner by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Owner.class);
    }

    // Add driver ID to Owner
    public Owner addDriverId(String ownerId, String driverId) {
        logger.info("Adding driver ID: {} to owner ID: {}", driverId, ownerId);
        if (findById(ownerId).isPresent()) {
            Owner owner = findById(ownerId).get();
            List<String> driverIds = owner.getDriverIds();
            driverIds.add(driverId);
            owner.setDriverIds(driverIds);
            return mongoTemplate.save(owner);
        }
        return null;
    }

    // Remove driver ID from Owner
    public Owner removeDriverId(String ownerId, String driverId) {
        logger.info("Removing driver ID: {} from owner ID: {}", driverId, ownerId);
        if (findById(ownerId).isPresent()) {
            Owner owner = findById(ownerId).get();
            List<String> driverIds = owner.getDriverIds();
            driverIds.remove(driverId);
            owner.setDriverIds(driverIds);
            return mongoTemplate.save(owner);
        }
        return null;
    }

    // Add vehicle ID to Owner
    public Owner addVehicleId(String ownerId, String vehicleId) {
        logger.info("Adding vehicle ID: {} to owner ID: {}", vehicleId, ownerId);
        if (findById(ownerId).isPresent()) {
            Owner owner = findById(ownerId).get();
            List<String> vehicleIds = owner.getVehicleIds();
            vehicleIds.add(vehicleId);
            owner.setVehicleIds(vehicleIds);
            return mongoTemplate.save(owner);
        }
        return null;
    }

    // Remove vehicle ID from Owner
    public Owner removeVehicleId(String ownerId, String vehicleId) {
        logger.info("Removing vehicle ID: {} from owner ID: {}", vehicleId, ownerId);
        if (findById(ownerId).isPresent()) {
            Owner owner = findById(ownerId).get();
            List<String> vehicleIds = owner.getVehicleIds();
            vehicleIds.remove(vehicleId);
            owner.setVehicleIds(vehicleIds);
            return mongoTemplate.save(owner);
        }
        return null;
    }

    // Add equipment ID to Owner
    public Owner addEquipmentId(String ownerId, String equipmentId) {
        logger.info("Adding equipment ID: {} to owner ID: {}", equipmentId, ownerId);
        if (findById(ownerId).isPresent()) {
            Owner owner = findById(ownerId).get();
            List<String> equipmentIds = owner.getEquipmentIds();
            equipmentIds.add(equipmentId);
            owner.setEquipmentIds(equipmentIds);
            return mongoTemplate.save(owner);
        }
        return null;
    }

    // Remove equipment ID from Owner
    public Owner removeEquipmentId(String ownerId, String equipmentId) {
        logger.info("Removing equipment ID: {} from owner ID: {}", equipmentId, ownerId);
        if (findById(ownerId).isPresent()) {
            Owner owner = findById(ownerId).get();
            List<String> equipmentIds = owner.getEquipmentIds();
            equipmentIds.remove(equipmentId);
            owner.setEquipmentIds(equipmentIds);
            return mongoTemplate.save(owner);
        }
        return null;
    }

}
