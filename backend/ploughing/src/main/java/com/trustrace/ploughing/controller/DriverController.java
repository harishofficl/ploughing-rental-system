package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.people.Driver;
import com.trustrace.ploughing.service.DriverService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${survey.build.version}/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    // Create Driver
    @PostMapping
    public ResponseEntity<ApiResponse<Driver>> createDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverService.createDriver(driver);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Driver created successfully", savedDriver));
    }

    // Get All Drivers
    @GetMapping
    public ResponseEntity<ApiResponse<List<Driver>>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Drivers retrieved successfully", drivers));
    }

    // Get Driver by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Driver>> getDriverById(@PathVariable String id) {
        Optional<Driver> driver = driverService.getDriverById(id);
        if (driver.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Driver not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Driver retrieved successfully", driver.get()));
    }

    // Get Drivers by Owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<Driver>>> getDriversByOwnerId(@PathVariable String ownerId) {
        List<Driver> drivers = driverService.getDriversByOwnerId(ownerId);
        if (drivers.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No drivers found for this owner", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Drivers retrieved successfully", drivers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Driver>> updateDriver(@PathVariable String id, @RequestBody Driver driver) {
        Optional<Driver> existingDriver = driverService.getDriverById(id);
        if (existingDriver.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Driver not found", null));
        }

        Driver updatedDriver = driverService.updateDriver(id, driver);
        return ResponseEntity.ok(new ApiResponse<>(true, "Driver updated successfully", updatedDriver));
    }

    // Delete Driver
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@PathVariable String id) {
        boolean isDeleted = driverService.deleteDriver(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Driver not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Driver deleted successfully", null));
    }

    @GetMapping("/owner/{ownerId}/paginated")
    public ResponseEntity<ApiResponse<Page<Driver>>> getDriversByOwnerIdPaginatedContainingName(
            @PathVariable String ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        Page<Driver> drivers = driverService.getDriversByOwnerIdPaginatedContainingName(ownerId, page, size, search);
        return ResponseEntity.ok(new ApiResponse<>(true, "Drivers retrieved successfully", drivers));
    }
}
