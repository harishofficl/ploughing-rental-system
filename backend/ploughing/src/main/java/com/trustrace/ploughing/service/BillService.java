package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.BillDao;
import com.trustrace.ploughing.model.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillDao billDao;

    public Bill saveBill(Bill bill) {
        return billDao.save(bill);
    }

    public Optional<Bill> getBillById(String id) {
        return billDao.findById(id);
    }

    public void deleteBillById(String id) {
        billDao.deleteById(id);
    }

    public List<Bill> getAllBills() {
        return billDao.findAll();
    }

    public List<Bill> getBillsByCustomerId(String customerId, boolean onlyUnpaid) {
        return billDao.findByCustomerId(customerId, onlyUnpaid);
    }
}