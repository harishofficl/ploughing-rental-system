package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.BillDao;
import com.trustrace.ploughing.dao.RentalRecordDao;
import com.trustrace.ploughing.model.Bill;
import com.trustrace.ploughing.view.BillView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillDao billDao;

    @Autowired
    private RentalRecordDao rentalRecordDao;

    public Bill saveBill(Bill bill) {
        return billDao.save(bill);
    }

    public Optional<Bill> getBillById(String id) {
        return billDao.findById(id);
    }

    public void deleteBillById(String id) {
        if (getBillById(id).isPresent()) {
            Bill bill = getBillById(id).get();
            List<String> rentalRecordIds = bill.getRentalRecordIds();
            rentalRecordIds.forEach(rentalRecordId -> rentalRecordDao.updateBilledStatus(rentalRecordId, false));
        }
        billDao.deleteById(id);
    }

    public List<Bill> getAllBills() {
        return billDao.findAll();
    }

    public List<Bill> getBillsByCustomerId(String customerId, boolean onlyUnpaid) {
        return billDao.findByCustomerId(customerId, onlyUnpaid);
    }

    public Bill updatePaymentId(String id, String paymentId) {
        return billDao.updatePaymentId(id, paymentId);
    }

    public Bill findByPaymentId(String paymentId) {
        return billDao.findByPaymentId(paymentId);
    }

    public void setBillRentalPaid(String id) {
        billDao.setBillRentalPaid(id);
    }

    public Page<BillView> getBillsByOwnerIdWithPagination(String ownerId, int page, int size, String search) {
        Page<Bill> bills = billDao.getBillsByOwnerIdWithPagination(ownerId, page, size, search);
        List<BillView> billViews = bills.getContent().stream().map(this::convertToBillView).collect(Collectors.toList());
        return new PageImpl<>(billViews, bills.getPageable(), bills.getTotalElements());
    }

    private BillView convertToBillView(Bill bill) {
        return new BillView(bill.getId(), bill.getCustomerName(), bill.getCreatedAt().toString(), bill.getRentalRecordIds().size(), bill.getTotalAmount(), bill.isPaid(), bill.getPaymentId(), bill.getCustomerId());
    }

}