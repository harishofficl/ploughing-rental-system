package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Equipment;
import com.trustrace.ploughing.service.EquipmentService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${ploughing.build.version}/api/equipments")
@PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
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

    // Update Equipment
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Equipment>> updateEquipment(@PathVariable String id, @RequestBody Equipment equipment) {
        Optional<Equipment> existingEquipment = equipmentService.getEquipmentById(id);
        if (existingEquipment.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Equipment not found", null));
        }
        Equipment updatedEquipment = equipmentService.updateEquipment(id, equipment);
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipment updated successfully", updatedEquipment));
    }

    // Update price of Equipment
    @PutMapping("/{id}/price")
    public ResponseEntity<ApiResponse<Equipment>> updatePrice(@PathVariable String id, @RequestParam int price) {
        Equipment updatedEquipment = equipmentService.updatePrice(id, price);
        if (updatedEquipment == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Equipment not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Price updated successfully", updatedEquipment));
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

    @GetMapping("/owner/{ownerId}/paginated")
    public ResponseEntity<ApiResponse<Page<Equipment>>> getEquipmentsByOwnerIdPaginatedContainingName(
            @PathVariable String ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        Page<Equipment> equipments = equipmentService.getEquipmentsByOwnerIdPaginatedContainingName(ownerId, page, size, search);
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipments retrieved successfully", equipments));
    }
}
