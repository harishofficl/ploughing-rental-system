package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Vehicle;
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
public class VehicleDao {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public Vehicle save(Vehicle vehicle) {
        logger.info("Saving Vehicle: {}", vehicle);
        return mongoTemplate.save(vehicle);
    }

    public List<Vehicle> findAll() {
        logger.info("Fetching all Vehicles");
        return mongoTemplate.findAll(Vehicle.class);
    }

    public Optional<Vehicle> findById(String id) {
        logger.info("Fetching Vehicle by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Vehicle.class));
    }

    public List<Vehicle> findByOwnerId(String ownerId) {
        logger.info("Fetching Vehicles for Owner ID: {}", ownerId);
        Query query = new Query(Criteria.where("ownerId").is(ownerId));
        return mongoTemplate.find(query, Vehicle.class);
    }

    // update fuel level
    public Vehicle updateFuelLevel(String id, float fuelLevel) {
        logger.info("Updating Fuel Level for Vehicle ID: {}", id);
        Vehicle vehicle = mongoTemplate.findById(id, Vehicle.class);
        if (vehicle != null) {
            vehicle.setCurrFuelLevel(fuelLevel);
            return mongoTemplate.save(vehicle);
        }
        return null;
    }

    public void deleteById(String id) {
        logger.info("Deleting Vehicle by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Vehicle.class);
    }
}
