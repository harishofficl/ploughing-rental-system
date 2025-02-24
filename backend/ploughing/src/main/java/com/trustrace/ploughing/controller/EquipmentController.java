package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Equipment;
import com.trustrace.ploughing.service.EquipmentService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${survey.build.version}/api/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    // Create Equipment
    @PostMapping
    public ResponseEntity<ApiResponse<Equipment>> createEquipment(@RequestBody Equipment equipment) {
        Equipment savedEquipment = equipmentService.createEquipment(equipment);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Equipment created successfully", savedEquipment));
    }

    // Get All Equipments
    @GetMapping
    public ResponseEntity<ApiResponse<List<Equipment>>> getAllEquipments() {
        List<Equipment> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipments retrieved successfully", equipments));
    }

    // Get Equipment by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Equipment>> getEquipmentById(@PathVariable String id) {
        Optional<Equipment> equipment = equipmentService.getEquipmentById(id);
        if (equipment.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Equipment not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipment retrieved successfully", equipment.get()));
    }

    // Get Equipments by Owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<Equipment>>> getEquipmentsByOwnerId(@PathVariable String ownerId) {
        List<Equipment> equipments = equipmentService.getEquipmentsByOwnerId(ownerId);
        if (equipments.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No equipments found for this owner", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipments retrieved successfully", equipments));
    }

    // Delete Equipment
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable String id) {
        boolean isDeleted = equipmentService.deleteEquipment(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Equipment not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipment deleted successfully", null));
    }
}
