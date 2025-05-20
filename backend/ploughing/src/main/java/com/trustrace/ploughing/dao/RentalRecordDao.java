package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.RentalRecord;
import com.trustrace.ploughing.model.people.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RentalRecordDao {

    private static final Logger logger = LoggerFactory.getLogger(RentalRecordDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EquipmentDao equipmentDao;

    @Autowired
    private CustomerDao customerDao;

    public RentalRecord save(RentalRecord rentalRecord) {
        logger.info("Saving RentalRecord: {}", rentalRecord);
        rentalRecord.setCreatedAt(LocalDateTime.now());
        rentalRecord.setEquipment(equipmentDao.findById(rentalRecord.getEquipment()).get().getName());
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

    public List<RentalRecord> findByCustomerId(String customerId) {
        logger.info("Fetching RentalRecords for Customer ID: {}", customerId);
        Query query = new Query(Criteria.where("customerId").is(customerId));
        return mongoTemplate.find(query, RentalRecord.class);
    }

    // Update RentalRecord by ID
    public RentalRecord updateById(String id, RentalRecord rentalRecord) {
        logger.info("Updating RentalRecord by ID: {}", id);
        rentalRecord.setId(id);
        rentalRecord.setCreatedAt(findById(id).isPresent() ? findById(id).get().getCreatedAt() : LocalDateTime.now());
        rentalRecord.setUpdatedAt(LocalDateTime.now());
        return mongoTemplate.save(rentalRecord);
    }

    // Set billed status
    public void updateBilledStatus(String id, Boolean billed) {
        logger.info("Setting record: {} to Unbilled", id);
        if(findById(id).isPresent()){
            RentalRecord rental = findById(id).get();
            rental.setBilled(billed);
            mongoTemplate.save(rental);
        }
    }

    public void deleteById(String id) {
        logger.info("Deleting RentalRecord by ID: {}", id);
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, RentalRecord.class);
    }

    // Get total pending rental amount for a given owner
    public double getTotalRentalAmountByOwnerId(String ownerId) {
        logger.info("Fetching total pending rental amount for Owner ID: {}", ownerId);
        Query query = new Query(Criteria.where("ownerId").is(ownerId).and("paid").is(false));
        List<RentalRecord> rentalRecords = mongoTemplate.find(query, RentalRecord.class);
        return rentalRecords.stream().mapToDouble(RentalRecord::getTotalCost).sum();
    }

    // Get rental records for customer id that are unpaid and un-billed
    public List<RentalRecord> getUnpaidRentalRecordsByCustomerId(String customerId) {
        logger.info("Fetching unpaid rental records for Customer ID: {}", customerId);
        Query query = new Query(Criteria.where("customerId").is(customerId).and("paid").is(false).and("billed").is(false));
        return mongoTemplate.find(query, RentalRecord.class);
    }

    // Get rental records for owner id with pagination
    public Page<RentalRecord> findByOwnerIdWithPagination(String ownerId, int page, int size, String search) {
        logger.info("Fetching RentalRecords for Owner ID: {} with pagination and search: {}", ownerId, search);
        Criteria criteria = Criteria.where("ownerId").is(ownerId);
        if (search != null && !search.isEmpty()) {
            criteria = criteria.and("customerId").in(customerDao.findByNameContaining(search).stream().map(Customer::getId).collect(Collectors.toList()));
        }
        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, RentalRecord.class);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        query.with(pageable);
        List<RentalRecord> rentalRecords = mongoTemplate.find(query, RentalRecord.class);
        return new PageImpl<>(rentalRecords, pageable, total);
    }
}
