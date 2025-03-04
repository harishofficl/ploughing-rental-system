package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.CustomerDao;
import com.trustrace.ploughing.dao.DriverDao;
import com.trustrace.ploughing.dao.RentalRecordDao;
import com.trustrace.ploughing.model.RentalRecord;
import com.trustrace.ploughing.view.RentalRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalRecordService {

    @Autowired
    private RentalRecordDao rentalRecordDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private CustomerDao customerDao;

    public RentalRecord createRentalRecord(RentalRecord rentalRecord) {
        return rentalRecordDao.save(rentalRecord);
    }

    public List<RentalRecord> getAllRentalRecords() {
        return rentalRecordDao.findAll();
    }

    public Optional<RentalRecord> getRentalRecordById(String id) {
        return rentalRecordDao.findById(id);
    }

    public List<RentalRecord> getRentalRecordsByOwnerId(String ownerId) {
        return rentalRecordDao.findByOwnerId(ownerId);
    }

    public List<RentalRecord> getRentalRecordsByDriverId(String driverId) {
        return rentalRecordDao.findByDriverId(driverId);
    }

    public List<RentalRecord> getRentalRecordsByCustomerId(String customerId) {
        return rentalRecordDao.findByCustomerId(customerId);
    }

    public RentalRecord updateRentalRecord(String id, RentalRecord rentalRecord) {
        return rentalRecordDao.updateById(id, rentalRecord);
    }

    public boolean deleteRentalRecord(String id) {
        Optional<RentalRecord> existingRecord = rentalRecordDao.findById(id);
        if (existingRecord.isPresent()) {
            rentalRecordDao.deleteById(id);
            return true;
        }
        return false;
    }

    public double getTotalRentalAmountByOwnerId(String ownerId) {
        return rentalRecordDao.getTotalRentalAmountByOwnerId(ownerId);
    }

    public List<RentalRecord> getUnpaidRentalRecordsByCustomerId(String customerId) {
        return rentalRecordDao.getUnpaidRentalRecordsByCustomerId(customerId);
    }

    public Page<RentalRecordView> getRentalRecordsByOwnerIdWithPagination(String ownerId, int page, int size, String search) {
        Page<RentalRecord> rentalRecords = rentalRecordDao.findByOwnerIdWithPagination(ownerId, page, size, search);
        List<RentalRecordView> rentalRecordViews = rentalRecords.getContent().stream()
                .map(this::convertToView)
                .collect(Collectors.toList());
        return new PageImpl<>(rentalRecordViews, rentalRecords.getPageable(), rentalRecords.getTotalElements());
    }

    private RentalRecordView convertToView(RentalRecord rentalRecord) {
        return new RentalRecordView(
                rentalRecord.getId(),
                rentalRecord.getOwnerId(),
                customerDao.findById(rentalRecord.getCustomerId()).get().getName(),
                driverDao.findById(rentalRecord.getDriverId()).get().getName(),
                rentalRecord.getDate().toString(),
                rentalRecord.getEquipment(),
                rentalRecord.getHoursUsed(),
                rentalRecord.getTotalCost(),
                rentalRecord.isPaid(),
                rentalRecord.isBilled()
        );
    }
}
