package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.DriverDao;
import com.trustrace.ploughing.model.people.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverDao driverDao;

    // Create or update a driver
    public Driver createDriver(Driver driver) {
        return driverDao.save(driver);
    }

    // Get all drivers
    public List<Driver> getAllDrivers() {
        return driverDao.findAll();
    }

    // Get a driver by ID
    public Optional<Driver> getDriverById(String id) {
        return driverDao.findById(id);
    }

    // Get drivers by Owner ID
    public List<Driver> getDriversByOwnerId(String ownerId) {
        return driverDao.findByOwnerId(ownerId);
    }

    // Update a driver by ID
    public Driver updateDriver(String id, Driver driver) {
        return driverDao.updateById(id, driver);
    }

    // Delete a driver by ID
    public boolean deleteDriver(String id) {
        Optional<Driver> existingDriver = driverDao.findById(id);
        if (existingDriver.isPresent()) {
            driverDao.deleteById(id);
            return true;
        }
        return false;
    }
}
