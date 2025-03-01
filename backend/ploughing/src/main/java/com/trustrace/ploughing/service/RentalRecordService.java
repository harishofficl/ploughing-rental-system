package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.RentalRecordDao;
import com.trustrace.ploughing.model.RentalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalRecordService {

    @Autowired
    private RentalRecordDao rentalRecordDao;

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
}
