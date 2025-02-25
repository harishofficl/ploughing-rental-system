package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Vehicle;
import com.trustrace.ploughing.service.VehicleService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${survey.build.version}/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Vehicle>> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Vehicle created successfully", savedVehicle));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicles retrieved successfully", vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable String id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        if (vehicle.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Vehicle not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicle retrieved successfully", vehicle.get()));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getVehiclesByOwnerId(@PathVariable String ownerId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
        if (vehicles.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No vehicles found for this owner", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicles retrieved successfully", vehicles));
    }

    @PutMapping("/{id}/update-fuel")
    public ResponseEntity<ApiResponse<Vehicle>> updateFuelLevel(@PathVariable String id, @RequestParam float updateFuel) {
        Vehicle updatedVehicle = vehicleService.updateFuelLevel(id, updateFuel, false);
        if (updatedVehicle == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Vehicle not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Fuel level updated successfully", updatedVehicle));
    }

    @PutMapping("/{id}/add-fuel")
    public ResponseEntity<ApiResponse<Vehicle>> addFuelLevel(@PathVariable String id, @RequestParam float addFuel) {
        Vehicle updatedVehicle = vehicleService.updateFuelLevel(id, addFuel, true);
        if (updatedVehicle == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Vehicle not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Fuel level updated successfully", updatedVehicle));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable String id) {
        boolean isDeleted = vehicleService.deleteVehicle(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Vehicle not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicle deleted successfully", null));
    }
}
