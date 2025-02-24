package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.RentalRecord;
import com.trustrace.ploughing.service.RentalRecordService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${survey.build.version}/api/rental-records")
public class RentalRecordController {

    @Autowired
    private RentalRecordService rentalRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<RentalRecord>> createRentalRecord(@RequestBody RentalRecord rentalRecord) {
        RentalRecord savedRecord = rentalRecordService.createRentalRecord(rentalRecord);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Rental record created successfully", savedRecord));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalRecord>>> getAllRentalRecords() {
        List<RentalRecord> rentalRecords = rentalRecordService.getAllRentalRecords();
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental records retrieved successfully", rentalRecords));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalRecord>> getRentalRecordById(@PathVariable String id) {
        Optional<RentalRecord> rentalRecord = rentalRecordService.getRentalRecordById(id);
        if (rentalRecord.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Rental record not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental record retrieved successfully", rentalRecord.get()));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<RentalRecord>>> getRentalRecordsByOwnerId(@PathVariable String ownerId) {
        List<RentalRecord> rentalRecords = rentalRecordService.getRentalRecordsByOwnerId(ownerId);
        if (rentalRecords.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No rental records found for this owner", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental records retrieved successfully", rentalRecords));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponse<List<RentalRecord>>> getRentalRecordsByDriverId(@PathVariable String driverId) {
        List<RentalRecord> rentalRecords = rentalRecordService.getRentalRecordsByDriverId(driverId);
        if (rentalRecords.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No rental records found for this driver", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental records retrieved successfully", rentalRecords));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRentalRecord(@PathVariable String id) {
        boolean isDeleted = rentalRecordService.deleteRentalRecord(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Rental record not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental record deleted successfully", null));
    }
}
