package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.people.Customer;
import com.trustrace.ploughing.service.CustomerService;
import com.trustrace.ploughing.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${survey.build.version}/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Create Customer
    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@RequestBody Customer customer) {
        Optional<Customer> existingCustomer = customerService.getCustomerByEmail(customer.getEmail());
        if (existingCustomer.isPresent()) {
            return ResponseEntity.status(409).body(new ApiResponse<>(false, "Customer with this email already exists", null));
        }

        Customer savedCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Customer created successfully", savedCustomer));
    }

    // Get All Customers
    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Customers retrieved successfully", customers));
    }

    // Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable String id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Customer not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Customer retrieved successfully", customer.get()));
    }

    // Get Customer by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Customer>> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        if (customer.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Customer not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Customer retrieved successfully", customer.get()));
    }

    // Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Optional<Customer> existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Customer not found", null));
        }

        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(new ApiResponse<>(true, "Customer updated successfully", updatedCustomer));
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable String id) {
        boolean isDeleted = customerService.deleteCustomer(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Customer not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Customer deleted successfully", null));
    }

    // findByOwnerIdAndContainsName -> /api/customers/owner/{ownerId}?search=${term}
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersByOwnerIdContainsName(@PathVariable String ownerId, @RequestParam String search) {
        if(ownerId == null || ownerId.isEmpty() ) {
            return ResponseEntity.status(400).body(new ApiResponse<>(false, "Owner ID is required", null));
        }
        if( search == null || search.isEmpty() ) {
            return ResponseEntity.status(200).body(new ApiResponse<>(true, "At least 1 char required to search", List.of()));
        }
        List<Customer> customers = customerService.getCustomersByOwnerIdContainsName(ownerId, search);
        if(customers.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse<>(true, "No customers found", List.of()));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Customers retrieved successfully", customers));
    }

    // countByOwnerId -> /api/customers/owner/{ownerId}/customers-count
    @GetMapping("/owner/{ownerId}/customers-count")
    public ResponseEntity<ApiResponse<Long>> getCustomerCountByOwnerId(@PathVariable String ownerId) {
        if(ownerId == null || ownerId.isEmpty() ) {
            return ResponseEntity.status(400).body(new ApiResponse<>(false, "Owner ID is required", null));
        }
        long count = customerService.getCustomerCountByOwnerId(ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Customers count retrieved successfully", count));
    }
}
