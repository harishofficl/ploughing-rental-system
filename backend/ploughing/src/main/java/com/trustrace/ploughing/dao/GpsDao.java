package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Gps;
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
public class GpsDao {

    private static final Logger logger = LoggerFactory.getLogger(GpsDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public Gps save(Gps gps) {
        logger.info("Saving GPS data: {}", gps);
        return mongoTemplate.save(gps);
    }

    public List<Gps> findAll() {
        logger.info("Fetching all GPS records");
        return mongoTemplate.findAll(Gps.class);
    }

    public Optional<Gps> findById(String id) {
        logger.info("Fetching GPS data by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Gps.class));
    }

    public List<Gps> findByDriverId(String driverId) {
        logger.info("Fetching GPS data for driver ID: {}", driverId);
        Query query = new Query(Criteria.where("driverId").is(driverId));
        return mongoTemplate.find(query, Gps.class);
    }

    public void deleteById(String id) {
        logger.info("Deleting GPS record by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Gps.class);
    }
}
