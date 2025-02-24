package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.EquipmentDao;
import com.trustrace.ploughing.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;

    // Create or update equipment
    public Equipment createEquipment(Equipment equipment) {
        return equipmentDao.save(equipment);
    }

    // Get all equipments
    public List<Equipment> getAllEquipments() {
        return equipmentDao.findAll();
    }

    // Get an equipment by ID
    public Optional<Equipment> getEquipmentById(String id) {
        return equipmentDao.findById(id);
    }

    // Get equipments by Owner ID
    public List<Equipment> getEquipmentsByOwnerId(String ownerId) {
        return equipmentDao.findByOwnerId(ownerId);
    }

    // Delete an equipment by ID
    public boolean deleteEquipment(String id) {
        Optional<Equipment> existingEquipment = equipmentDao.findById(id);
        if (existingEquipment.isPresent()) {
            equipmentDao.deleteById(id);
            return true;
        }
        return false;
    }
}
