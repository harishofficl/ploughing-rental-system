package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.PaymentDao;
import com.trustrace.ploughing.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    public Payment savePayment(Payment payment) {
        return paymentDao.savePayment(payment);
    }

    public Payment getPaymentById(String id) {
        return paymentDao.getPaymentById(id);
    }

    public List<Payment> getAllPayments() {
        return paymentDao.getAllPayments();
    }

    public List<Payment> getPaymentsByOwnerId(String ownerId) {
        return paymentDao.getPaymentsByOwnerId(ownerId);
    }
}
