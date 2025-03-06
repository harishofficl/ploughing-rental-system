package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Vehicle;
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
public class VehicleDao {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OwnerDao ownerDao;

    public Vehicle save(Vehicle vehicle) {
        logger.info("Saving Vehicle: {}", vehicle);
        vehicle.setCurrFuelLevel(0);
        Vehicle createdVehicle = mongoTemplate.save(vehicle);
        ownerDao.addVehicleId(createdVehicle.getOwnerId(), createdVehicle.getId());
        return createdVehicle;
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
    public Vehicle updateFuelLevel(String id, double fuelLevel, boolean add) {
        logger.info("Updating Fuel Level for Vehicle ID: {}", id);
        Vehicle vehicle = mongoTemplate.findById(id, Vehicle.class);
        if (vehicle == null) {
            logger.info("Vehicle ID: {} not found", id);
            return null;
        }
        if (add) {
            vehicle.setCurrFuelLevel(vehicle.getCurrFuelLevel() + fuelLevel);
        } else {
            vehicle.setCurrFuelLevel(fuelLevel);
        }
        return mongoTemplate.save(vehicle);
    }


    public void deleteById(String id) {
        logger.info("Deleting Vehicle by ID: {}", id);
        if (findById(id).isEmpty()) {
            logger.info("Vehicle with ID: {} not found", id);
            return;
        }
        Query query = new Query(Criteria.where("id").is(id));
        ownerDao.removeVehicleId(findById(id).get().getOwnerId(), id);
        mongoTemplate.remove(query, Vehicle.class);
    }

    public Page<Vehicle> getVehiclesByOwnerIdPaginatedContainingName(String ownerId, int page, int size, String search) {
        Criteria criteria = Criteria.where("ownerId").is(ownerId);
        if (search != null && !search.isEmpty()) {
            criteria.and("name").regex(search, "i");
        }
        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Vehicle.class);
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);
        List<Vehicle> vehicles = mongoTemplate.find(query, Vehicle.class);
        return new PageImpl<>(vehicles, pageable, total);
    }
}
