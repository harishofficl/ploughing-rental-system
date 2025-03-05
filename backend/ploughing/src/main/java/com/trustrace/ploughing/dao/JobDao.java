package com.trustrace.ploughing.dao;

import com.trustrace.ploughing.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JobDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DriverDao driverDao;

    // create job
    public Job save(Job job) {
        job.setStartTime(LocalDateTime.now());
        Job createdJob = mongoTemplate.save(job);
        if (driverDao.findById(createdJob.getDriverId()).isPresent()) {
            driverDao.findById(createdJob.getDriverId()).get().setCurrentJobId(createdJob.getId());
        }
        return createdJob;
    }

    // find job by id
    public Job findById(String id) {
        return mongoTemplate.findById(id, Job.class);
    }

    // find jobs by driver id and completed status and today's date
    public List<Job> findTodayCompletedJobsByDriverId(String driverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("driverId").is(driverId)
                .and("completed").is(true)
                .and("startTime").gte(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
                .and("startTime").lte(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)));
        return mongoTemplate.find(query, Job.class);
    }

    // update job by id
    public Job updateById(String id, Job job) {
        Job existingJob = findById(id);
        if (existingJob == null) {
            return null;
        }
        job.setId(id);
        return mongoTemplate.save(job);
    }

    // delete job by id
    public void deleteById(String id) {
        Job existingJob = findById(id);
        if (existingJob != null) {
            mongoTemplate.remove(existingJob);
        }
    }

    // end job by id
    public Job endJobById(String id, String endImagePath, double dieselUsed) {
        Job existingJob = findById(id);
        if (existingJob == null) {
            return null;
        }
        existingJob.setEndTime(LocalDateTime.now());
        existingJob.setEndImagePath(endImagePath);
        existingJob.setDieselUsed(dieselUsed);
        existingJob.setCompleted(true);
        if (driverDao.findById(existingJob.getDriverId()).isPresent()) {
            driverDao.findById(existingJob.getDriverId()).get().setCurrentJobId("idle");
        }
        return mongoTemplate.save(existingJob);
    }
}
