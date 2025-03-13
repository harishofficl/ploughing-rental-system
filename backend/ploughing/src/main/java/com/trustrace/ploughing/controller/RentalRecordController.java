package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.RentalRecord;
import com.trustrace.ploughing.service.RentalRecordService;
import com.trustrace.ploughing.dto.ApiResponse;
import com.trustrace.ploughing.view.RentalRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${ploughing.build.version}/api/rental-records")
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

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<RentalRecord>>> getRentalRecordsByCustomerId(@PathVariable String customerId) {
        List<RentalRecord> rentalRecords = rentalRecordService.getRentalRecordsByCustomerId(customerId);
        if (rentalRecords.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "No rental records found for this customer", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental records retrieved successfully", rentalRecords));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalRecord>> updateRentalRecord(@PathVariable String id, @RequestBody RentalRecord rentalRecord) {
        Optional<RentalRecord> existingRecord = rentalRecordService.getRentalRecordById(id);
        if (existingRecord.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Rental record not found", null));
        }

        RentalRecord updatedRecord = rentalRecordService.updateRentalRecord(id, rentalRecord);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental record updated successfully", updatedRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRentalRecord(@PathVariable String id) {
        boolean isDeleted = rentalRecordService.deleteRentalRecord(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Rental record not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental record deleted successfully", null));
    }

    @GetMapping("/owner/{ownerId}/total-outstanding-amount")
    public ResponseEntity<ApiResponse<Double>> getTotalRentalAmountByOwnerId(@PathVariable String ownerId) {
        double totalAmount = rentalRecordService.getTotalRentalAmountByOwnerId(ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Total rental amount retrieved successfully", totalAmount));
    }

    @GetMapping("/customer/{customerId}/unpaid-records")
    public ResponseEntity<ApiResponse<List<RentalRecord>>> getUnpaidRentalRecordsByCustomerId(@PathVariable String customerId) {
        List<RentalRecord> rentalRecords = rentalRecordService.getUnpaidRentalRecordsByCustomerId(customerId);
        if (rentalRecords.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No unpaid rental records found for this customer", List.of()));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Unpaid rental records retrieved successfully", rentalRecords));
    }

    @GetMapping("/owner/{ownerId}/paginated")
    public ResponseEntity<ApiResponse<Page<RentalRecordView>>> getRentalRecordsByOwnerIdWithPagination(@PathVariable String ownerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String search) {
        Page<RentalRecordView> rentalRecords = rentalRecordService.getRentalRecordsByOwnerIdWithPagination(ownerId, page, size, search);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental records retrieved successfully", rentalRecords));
    }
}
