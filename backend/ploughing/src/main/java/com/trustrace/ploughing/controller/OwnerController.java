package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Equipment;
import com.trustrace.ploughing.model.Vehicle;
import com.trustrace.ploughing.model.people.Driver;
import com.trustrace.ploughing.model.people.Owner;
import com.trustrace.ploughing.service.OwnerService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${ploughing.build.version}/api/owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    // Create Owner
    @PostMapping
    public ResponseEntity<ApiResponse<Owner>> createOwner(@RequestBody Owner owner) {
        Optional<Owner> existingOwner = ownerService.getOwnerByEmail(owner.getEmail());
        if (existingOwner.isPresent()) {
            return ResponseEntity.status(409).body(new ApiResponse<>(false, "Owner with this email already exists", null));
        }

        Owner savedOwner = ownerService.createOwner(owner);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Owner created successfully", savedOwner));
    }

    // Get All Owners
    @GetMapping
    public ResponseEntity<ApiResponse<List<Owner>>> getAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        return ResponseEntity.ok(new ApiResponse<>(true, "Owners retrieved successfully", owners));
    }

    // Get Owner by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Owner>> getOwnerById(@PathVariable String id) {
        Optional<Owner> owner = ownerService.getOwnerById(id);
        if (owner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Owner retrieved successfully", owner.get()));
    }

    // Get Owner by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Owner>> getOwnerByEmail(@PathVariable String email) {
        Optional<Owner> owner = ownerService.getOwnerByEmail(email);
        if (owner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Owner retrieved successfully", owner.get()));
    }

    // Update Owner
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Owner>> updateOwner(@PathVariable String id, @RequestBody Owner owner) {
        Optional<Owner> existingOwner = ownerService.getOwnerById(id);
        if (existingOwner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }

        Owner updatedOwner = ownerService.updateOwner(id, owner);
        return ResponseEntity.ok(new ApiResponse<>(true, "Owner updated successfully", updatedOwner));
    }

    // Delete Owner
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOwner(@PathVariable String id) {
        boolean isDeleted = ownerService.deleteOwner(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Owner deleted successfully", null));
    }

    // Add Vehicle to Owner
    @PutMapping("/{ownerId}/vehicles/{vehicleId}")
    public ResponseEntity<ApiResponse<Owner>> addVehicle(@PathVariable String ownerId, @PathVariable String vehicleId) {
        Owner owner = ownerService.addVehicle(ownerId, vehicleId);
        if (owner == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicle added to owner successfully", owner));
    }

    // Remove Vehicle from Owner
    @DeleteMapping("/{ownerId}/vehicles/{vehicleId}")
    public ResponseEntity<ApiResponse<Owner>> removeVehicle(@PathVariable String ownerId, @PathVariable String vehicleId) {
        Owner owner = ownerService.removeVehicle(ownerId, vehicleId);
        if (owner == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicle removed from owner successfully", owner));
    }

    // Add Driver to Owner
    @PutMapping("/{ownerId}/drivers/{driverId}")
    public ResponseEntity<ApiResponse<Owner>> addDriver(@PathVariable String ownerId, @PathVariable String driverId) {
        Owner owner = ownerService.addDriver(ownerId, driverId);
        if (owner == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Driver added to owner successfully", owner));
    }

    // Remove Driver from Owner
    @DeleteMapping("/{ownerId}/drivers/{driverId}")
    public ResponseEntity<ApiResponse<Owner>> removeDriver(@PathVariable String ownerId, @PathVariable String driverId) {
        Owner owner = ownerService.removeDriver(ownerId, driverId);
        if (owner == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Driver removed from owner successfully", owner));
    }

    // Add Equipment to Owner
    @PutMapping("/{ownerId}/equipment/{equipmentId}")
    public ResponseEntity<ApiResponse<Owner>> addEquipment(@PathVariable String ownerId, @PathVariable String equipmentId) {
        Owner owner = ownerService.addEquipment(ownerId, equipmentId);
        if (owner == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipment added to owner successfully", owner));
    }

    // Remove Equipment from Owner
    @DeleteMapping("/{ownerId}/equipment/{equipmentId}")
    public ResponseEntity<ApiResponse<Owner>> removeEquipment(@PathVariable String ownerId, @PathVariable String equipmentId) {
        Owner owner = ownerService.removeEquipment(ownerId, equipmentId);
        if (owner == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipment removed from owner successfully", owner));
    }

    // Get all equipments by their ids
    @GetMapping("/{ownerId}/equipments")
    public ResponseEntity<ApiResponse<List<Equipment>>> getEquipmentsByOwnerId(@PathVariable String ownerId) {
        Optional<Owner> owner = ownerService.getOwnerById(ownerId);
        if (owner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        List<Equipment> equipments = ownerService.getEquipmentsByIds(owner.get().getEquipmentIds());
        return ResponseEntity.ok(new ApiResponse<>(true, "Equipments retrieved successfully", equipments));
    }

    // Get all vehicles by their ids
    @GetMapping("/{ownerId}/vehicles")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getVehiclesByOwnerId(@PathVariable String ownerId) {
        Optional<Owner> owner = ownerService.getOwnerById(ownerId);
        if (owner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        List<Vehicle> vehicles = ownerService.getVehiclesByIds(owner.get().getVehicleIds());
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicles retrieved successfully", vehicles));
    }

    // Get all drivers by their ids
    @GetMapping("/{ownerId}/drivers")
    public ResponseEntity<ApiResponse<List<Driver>>> getDriversByOwnerId(@PathVariable String ownerId) {
        Optional<Owner> owner = ownerService.getOwnerById(ownerId);
        if (owner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        List<Driver> drivers = ownerService.getDriversByIds(owner.get().getDriverIds());
        return ResponseEntity.ok(new ApiResponse<>(true, "Drivers retrieved successfully", drivers));
    }

    // get distance pricing rules
    @GetMapping("/{ownerId}/distance-pricing-rules")
    public ResponseEntity<ApiResponse<List<Object>>> getDistancePricingRules(@PathVariable String ownerId) {
        Optional<Owner> owner = ownerService.getOwnerById(ownerId);
        if (owner.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Distance pricing rules retrieved successfully", owner.get().getDistancePricingRules()));
    }

    // Update distance pricing rules
    @PutMapping("/{ownerId}/distance-pricing-rules")
    public ResponseEntity<ApiResponse<List<Object>>> updateDistancePricingRules(@PathVariable String ownerId, @RequestBody List<Object> distancePricingRules) {
        List<Object> updatedDistancePricingRules = ownerService.updateDistancePricingRules(ownerId, distancePricingRules);
        return ResponseEntity.ok(new ApiResponse<>(true, "Distance pricing rules updated successfully", updatedDistancePricingRules));
    }
}
