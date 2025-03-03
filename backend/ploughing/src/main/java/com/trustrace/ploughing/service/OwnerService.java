package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.OwnerDao;
import com.trustrace.ploughing.model.Equipment;
import com.trustrace.ploughing.model.Vehicle;
import com.trustrace.ploughing.model.people.Driver;
import com.trustrace.ploughing.model.people.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private OwnerDao ownerDao;

    // Create or update an owner
    public Owner createOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    // Get all owners
    public List<Owner> getAllOwners() {
        return ownerDao.findAll();
    }

    // Get an owner by ID
    public Optional<Owner> getOwnerById(String id) {
        return ownerDao.findById(id);
    }

    // Get an owner by email
    public Optional<Owner> getOwnerByEmail(String email) {
        return ownerDao.findByEmail(email);
    }

    // Update an owner by ID
    public Owner updateOwner(String id, Owner owner) {
        return ownerDao.updateById(id, owner);
    }

    // Delete an owner by ID
    public boolean deleteOwner(String id) {
        Optional<Owner> existingOwner = ownerDao.findById(id);
        if (existingOwner.isPresent()) {
            ownerDao.deleteById(id);
            return true;
        }
        return false;
    }

    // Add a vehicle to an owner
    public Owner addVehicle(String ownerId, String vehicleId) {
        return ownerDao.addVehicleId(ownerId, vehicleId);
    }

    // Remove a vehicle from an owner
    public Owner removeVehicle(String ownerId, String vehicleId) {
        return ownerDao.removeVehicleId(ownerId, vehicleId);
    }

    // Add a driver to an owner
    public Owner addDriver(String ownerId, String driverId) {
        return ownerDao.addDriverId(ownerId, driverId);
    }

    // Remove a driver from an owner
    public Owner removeDriver(String ownerId, String driverId) {
        return ownerDao.removeDriverId(ownerId, driverId);
    }

    // Add equipment to an owner
    public Owner addEquipment(String ownerId, String equipmentId) {
        return ownerDao.addEquipmentId(ownerId, equipmentId);
    }

    // Remove equipment from an owner
    public Owner removeEquipment(String ownerId, String equipmentId) {
        return ownerDao.removeEquipmentId(ownerId, equipmentId);
    }

    // get all equipments by their ids
    public List<Equipment> getEquipmentsByIds(List<String> equipmentIds) {
        return ownerDao.getEquipmentsByIds(equipmentIds);
    }

    // get all vehicles by their ids
    public List<Vehicle> getVehiclesByIds(List<String> vehicleIds) {
        return ownerDao.getVehiclesByIds(vehicleIds);
    }

    // get all drivers by their ids
    public List<Driver> getDriversByIds(List<String> driverIds) {
        return ownerDao.getDriversByIds(driverIds);
    }

    // update distance pricing rules
    public List<Object> updateDistancePricingRules(String ownerId, List<Object> distancePricingRules) {
        return ownerDao.updateDistancePricingRules(ownerId, distancePricingRules);
    }
}
