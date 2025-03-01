package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Bill;
import com.trustrace.ploughing.model.RentalRecord;
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
public class BillDao {

    private static final Logger logger = LoggerFactory.getLogger(BillDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // Save Bill
    public Bill save(Bill bill) {
        logger.info("Saving bill with id: {}", bill.getId());
        bill.setCreatedAt(LocalDateTime.now());
        List<String> rentalRecordIds= bill.getRentalRecordIds();
        rentalRecordIds.forEach(rentalRecordId -> {
            logger.info("Updating rental record to billed with ID: {}", rentalRecordId);
            RentalRecord rentalRecord = mongoTemplate.findById(rentalRecordId, RentalRecord.class);
            assert rentalRecord != null;
            rentalRecord.setBilled(true);
            mongoTemplate.save(rentalRecord);
        });
        return mongoTemplate.save(bill);
    }

    // Find Bill by ID
    public Optional<Bill> findById(String id) {
        logger.info("Fetching bill by ID: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, Bill.class));
    }

    // Delete Bill by ID
    public void deleteById(String id) {
        logger.info("Deleting bill by ID: {}", id);
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)), Bill.class);
    }

    // Get All Bills
    public List<Bill> findAll() {
        logger.info("Fetching all bills");
        return mongoTemplate.findAll(Bill.class);
    }

    // Get Bills by customer ID
    public List<Bill> findByCustomerId(String customerId, boolean onlyUnpaid) {
        logger.info("Fetching bills by customer ID: {}", customerId);
        if(onlyUnpaid) {
            Query query = new Query(Criteria.where("customerId").is(customerId).and("paid").is(false));
            return mongoTemplate.find(query, Bill.class);
        }
        Query query = new Query(Criteria.where("customerId").is(customerId));
        return mongoTemplate.find(query, Bill.class);
    }


}
