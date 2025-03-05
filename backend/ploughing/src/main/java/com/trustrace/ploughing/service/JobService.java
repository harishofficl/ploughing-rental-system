package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.JobDao;
import com.trustrace.ploughing.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobDao jobDao;

    public Job createJob(Job job) {
        return jobDao.save(job);
    }

    public Job getJobById(String id) {
        return jobDao.findById(id);
    }

    public List<Job> findTodayCompletedJobsByDriverId(String driverId) {
        return jobDao.findTodayCompletedJobsByDriverId(driverId);
    }

    public Job updateJob(String id, Job job) {
        return jobDao.updateById(id, job);
    }

    public void deleteJob(String id) {
        jobDao.deleteById(id);
    }

    public Job endJob(String id, String endImagePath, double dieselUsed) {
        return jobDao.endJobById(id, endImagePath, dieselUsed);
    }
}
