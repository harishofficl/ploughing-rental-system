package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.EquipmentDao;
import com.trustrace.ploughing.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    // Get equipment by ID
    public Optional<Equipment> getEquipmentById(String id) {
        return equipmentDao.findById(id);
    }

    // Get equipments by Owner ID
    public List<Equipment> getEquipmentsByOwnerId(String ownerId) {
        return equipmentDao.findByOwnerId(ownerId);
    }

    // Update equipment by ID
    public Equipment updateEquipment(String id, Equipment equipment) {
        return equipmentDao.updateById(id, equipment);
    }

    // Update price of equipment
    public Equipment updatePrice(String id, int price) {
        return equipmentDao.updatePrice(id, price);
    }

    // Delete equipment by ID
    public boolean deleteEquipment(String id) {
        Optional<Equipment> existingEquipment = equipmentDao.findById(id);
        if (existingEquipment.isPresent()) {
            equipmentDao.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Equipment> getEquipmentsByOwnerIdPaginatedContainingName(String ownerId, int page, int size, String search) {
        return equipmentDao.getEquipmentsByOwnerIdPaginatedContainingName(ownerId, page, size, search);
    }
}
