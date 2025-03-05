package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.dto.ApiResponse;
import com.trustrace.ploughing.model.Job;
import com.trustrace.ploughing.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${survey.build.version}/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<Job>> createJob(@RequestBody Job job) {
        Job savedJob = jobService.createJob(job);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Job created successfully", savedJob));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Job>> getJobById(@PathVariable String id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job retrieved successfully", job));
    }

    @GetMapping("/driver/{driverId}/today-completed")
    public ResponseEntity<ApiResponse<List<Job>>> getTodayCompletedJobsByDriverId(@PathVariable String driverId) {
        List<Job> jobs = jobService.findTodayCompletedJobsByDriverId(driverId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Jobs retrieved successfully", jobs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Job>> updateJob(@PathVariable String id,@RequestBody Job job) {
        Job updatedJob = jobService.updateJob(id, job);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job updated successfully", updatedJob));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job deleted successfully", null));
    }

    @PutMapping("/{id}/end-job")
    public ResponseEntity<ApiResponse<Job>> endJob(@PathVariable String id,@RequestParam String endImagePath,@RequestParam double dieselUsed) {
        Job endedJob = jobService.endJob(id, endImagePath, dieselUsed);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job ended successfully", endedJob));
    }
}
