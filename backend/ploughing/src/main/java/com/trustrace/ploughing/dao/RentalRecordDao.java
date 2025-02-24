package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.RentalRecord;
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
public class RentalRecordDao {

    private static final Logger logger = LoggerFactory.getLogger(RentalRecordDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public RentalRecord save(RentalRecord rentalRecord) {
        logger.info("Saving RentalRecord: {}", rentalRecord);
        return mongoTemplate.save(rentalRecord);
    }

    public List<RentalRecord> findAll() {
        logger.info("Fetching all RentalRecords");
        return mongoTemplate.findAll(RentalRecord.class);
    }

    public Optional<RentalRecord> findById(String id) {
        logger.info("Fetching RentalRecord by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, RentalRecord.class));
    }

    public List<RentalRecord> findByOwnerId(String ownerId) {
        logger.info("Fetching RentalRecords for Owner ID: {}", ownerId);
        Query query = new Query(Criteria.where("ownerId").is(ownerId));
        return mongoTemplate.find(query, RentalRecord.class);
    }

    public List<RentalRecord> findByDriverId(String driverId) {
        logger.info("Fetching RentalRecords for Driver ID: {}", driverId);
        Query query = new Query(Criteria.where("driverId").is(driverId));
        return mongoTemplate.find(query, RentalRecord.class);
    }

    public void deleteById(String id) {
        logger.info("Deleting RentalRecord by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, RentalRecord.class);
    }
}
