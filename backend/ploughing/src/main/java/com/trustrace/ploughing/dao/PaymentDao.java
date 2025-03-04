package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Repository
public class PaymentDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Payment savePayment(Payment payment) {
        log.info("Saving payment details for bill id: {}", payment.getBillId());
        return mongoTemplate.save(payment);
    }

    public Payment getPaymentById(String id) {
        log.info("Fetching payment details by ID: {}", id);
        return mongoTemplate.findById(id, Payment.class);
    }

    public List<Payment> getAllPayments() {
        log.info("Fetching all payments");
        return mongoTemplate.findAll(Payment.class);
    }

    public List<Payment> getPaymentsByOwnerId(String ownerId) {
        log.info("Fetching payments by owner ID: {}", ownerId);
        return mongoTemplate.find(query(where("ownerId").is(ownerId)), Payment.class);
    }
}
