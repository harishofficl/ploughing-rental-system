package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.dto.ApiResponse;
import com.trustrace.ploughing.model.people.Admin;
import com.trustrace.ploughing.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${ploughing.build.version}/api/admins")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Create Admin
    @PostMapping
    public ResponseEntity<ApiResponse<Admin>> createAdmin(@RequestBody Admin admin) {
        Optional<Admin> existingAdmin = adminService.getAdminByEmail(admin.getEmail());
        if (existingAdmin.isPresent()) {
            return ResponseEntity.status(409).body(new ApiResponse<>(false, "Admin with this email already exists", null));
        }

        Admin savedAdmin = adminService.createAdmin(admin);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Admin created successfully", savedAdmin));
    }

    // Get All Admins
    @GetMapping
    public ResponseEntity<ApiResponse<List<Admin>>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(new ApiResponse<>(true, "Admins retrieved successfully", admins));
    }

    // Get Admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Admin>> getAdminById(@PathVariable String id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        if (admin.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Admin not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin retrieved successfully", admin.get()));
    }

    // Get Admin by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Admin>> getAdminByEmail(@PathVariable String email) {
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        if (admin.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Admin not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin retrieved successfully", admin.get()));
    }

    // Update Admin
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Admin>> updateAdmin(@PathVariable String id, @RequestBody Admin admin) {
        Optional<Admin> existingAdmin = adminService.getAdminById(id);
        if (existingAdmin.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Admin not found", null));
        }

        Admin updatedAdmin = adminService.updateAdmin(id, admin);
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin updated successfully", updatedAdmin));
    }

    // Delete Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(@PathVariable String id) {
        boolean isDeleted = adminService.deleteAdmin(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Admin not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin deleted successfully", null));
    }
}
