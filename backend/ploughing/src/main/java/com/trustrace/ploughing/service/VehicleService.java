package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.VehicleDao;
import com.trustrace.ploughing.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleDao vehicleDao;

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleDao.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDao.findAll();
    }

    public Optional<Vehicle> getVehicleById(String id) {
        return vehicleDao.findById(id);
    }

    public List<Vehicle> getVehiclesByOwnerId(String ownerId) {
        return vehicleDao.findByOwnerId(ownerId);
    }

    //update fuel level
    public Vehicle updateFuelLevel(String id, float fuelLevel, boolean add) {
        return vehicleDao.updateFuelLevel(id, fuelLevel, add);
    }

    public boolean deleteVehicle(String id) {
        Optional<Vehicle> existingVehicle = vehicleDao.findById(id);
        if (existingVehicle.isPresent()) {
            vehicleDao.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Vehicle> getVehiclesByOwnerIdPaginatedContainingName(String ownerId, int page, int size, String search) {
        return vehicleDao.getVehiclesByOwnerIdPaginatedContainingName(ownerId, page, size, search);
    }
}
