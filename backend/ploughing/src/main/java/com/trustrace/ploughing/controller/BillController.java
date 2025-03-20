package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Bill;
import com.trustrace.ploughing.service.BillService;
import com.trustrace.ploughing.dto.ApiResponse;
import com.trustrace.ploughing.view.BillView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${ploughing.build.version}/api/bills")
@PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping
    public ResponseEntity<ApiResponse<Bill>> saveBill(@RequestBody Bill bill) {
        Bill savedBill = billService.saveBill(bill);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Bill created successfully", savedBill));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Bill>> getBillById(@PathVariable String id) {
        Bill bill = billService.getBillById(id).orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        return ResponseEntity.ok(new ApiResponse<>(true, "Bill retrieved successfully", bill));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Bill>>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(new ApiResponse<>(true, "Bills retrieved successfully", bills));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBillById(@PathVariable String id) {
        billService.deleteBillById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Bill deleted successfully", null));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Bill>>> getBillsByCustomerId(@PathVariable String customerId) {
        List<Bill> bills = billService.getBillsByCustomerId(customerId, false);
        return ResponseEntity.ok(new ApiResponse<>(true, "Bills retrieved successfully", bills));
    }

    @GetMapping("/customer/{customerId}/unpaid")
    public ResponseEntity<ApiResponse<List<Bill>>> getUnpaidBillsByCustomerId(@PathVariable String customerId) {
        List<Bill> bills = billService.getBillsByCustomerId(customerId, true);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unpaid bills retrieved successfully", bills));
    }

    @GetMapping("/owner/{ownerId}/paginated")
    public ResponseEntity<ApiResponse<Page<BillView>>> getBillsByOwnerIdWithPagination(@PathVariable String ownerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String search) {
        Page<BillView> bills = billService.getBillsByOwnerIdWithPagination(ownerId, page, size, search);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rental records retrieved successfully", bills));
    }
}