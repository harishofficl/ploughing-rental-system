package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Gps;
import com.trustrace.ploughing.service.GpsService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${ploughing.build.version}/api/gps")
@PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
public class GpsController {

    @Autowired
    private GpsService gpsService;

    @PostMapping
    public ResponseEntity<ApiResponse<Gps>> createGpsData(@RequestBody Gps gps) {
        Gps savedGps = gpsService.createGpsData(gps);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "GPS data recorded successfully", savedGps));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Gps>>> getAllGpsData() {
        List<Gps> gpsData = gpsService.getAllGpsData();
        return ResponseEntity.ok(new ApiResponse<>(true, "GPS data retrieved successfully", gpsData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Gps>> getGpsDataById(@PathVariable String id) {
        Optional<Gps> gps = gpsService.getGpsDataById(id);
        if (gps.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "GPS data not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "GPS data retrieved successfully", gps.get()));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponse<List<Gps>>> getGpsDataByDriverId(@PathVariable String driverId) {
        List<Gps> gpsData = gpsService.getGpsDataByDriverId(driverId);
        if (gpsData.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No GPS data found for this driver", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "GPS data retrieved successfully", gpsData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGpsData(@PathVariable String id) {
        boolean isDeleted = gpsService.deleteGpsData(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "GPS data not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "GPS data deleted successfully", null));
    }
}
