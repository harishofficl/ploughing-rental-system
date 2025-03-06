package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class JobDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    // create job
    public Job save(Job job) {
        log.info("Creating job for driver: {}", job.getDriverId());
        job.setStartTime(LocalDateTime.now());
        Job createdJob = mongoTemplate.save(job);
        driverDao.updateCurrentJobId(job.getDriverId(), createdJob.getId());
        return createdJob;
    }

    // find job by id
    public Job findById(String id) {
        log.info("Finding job by id: {}", id);
        return mongoTemplate.findById(id, Job.class);
    }

    // find jobs by driver id and completed status and today's date
    public List<Job> findTodayCompletedJobsByDriverId(String driverId) {
        log.info("Finding today's completed jobs for driver: {}", driverId);
        Query query = new Query();
        query.addCriteria(Criteria.where("driverId").is(driverId)
                .and("completed").is(true)
                .and("startTime").gte(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
                .and("endTime").lte(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)));
        return mongoTemplate.find(query, Job.class);
    }

    // update job by id
    public Job updateById(String id, Job job) {
        log.info("Updating job by id: {}", id);
        Job existingJob = findById(id);
        if (existingJob == null) {
            return null;
        }
        job.setId(id);
        return mongoTemplate.save(job);
    }

    // delete job by id
    public void deleteById(String id) {
        log.info("Deleting job by id: {}", id);
        Job existingJob = findById(id);
        if (existingJob != null) {
            mongoTemplate.remove(existingJob);
        }
    }

    // end job by id
    public Job endJobById(String id, String endImagePath, double dieselUsed) {
        log.info("Ending job by id: {}", id);
        Job existingJob = findById(id);
        if (existingJob == null) {
            return null;
        }
        existingJob.setEndTime(LocalDateTime.now());
        existingJob.setEndImagePath(endImagePath);
        existingJob.setDieselUsed(dieselUsed);
        existingJob.setCompleted(true);
        // set total time taken for the job
        int totalTimeTaken = (int) Duration.between(existingJob.getStartTime(), existingJob.getEndTime()).getSeconds();
        existingJob.setTotalTimeInSeconds(totalTimeTaken);
        driverDao.updateCurrentJobId(existingJob.getDriverId(), "idle");
        vehicleDao.updateFuelLevel(existingJob.getVehicleId(), -(dieselUsed), true);
        return mongoTemplate.save(existingJob);
    }
}
