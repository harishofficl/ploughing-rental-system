package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.people.Owner;
import com.trustrace.ploughing.service.OwnerService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${survey.build.version}/api/owners")
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

    // Delete Owner
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOwner(@PathVariable String id) {
        boolean isDeleted = ownerService.deleteOwner(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Owner not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Owner deleted successfully", null));
    }
}
