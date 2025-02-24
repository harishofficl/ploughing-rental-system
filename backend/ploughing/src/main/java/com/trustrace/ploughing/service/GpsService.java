package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.GpsDao;
import com.trustrace.ploughing.model.Gps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GpsService {

    @Autowired
    private GpsDao gpsDao;

    public Gps createGpsData(Gps gps) {
        return gpsDao.save(gps);
    }

    public List<Gps> getAllGpsData() {
        return gpsDao.findAll();
    }

    public Optional<Gps> getGpsDataById(String id) {
        return gpsDao.findById(id);
    }

    public List<Gps> getGpsDataByDriverId(String driverId) {
        return gpsDao.findByDriverId(driverId);
    }

    public List<Gps> getGpsDataByVehicleId(String vehicleId) {
        return gpsDao.findByVehicleId(vehicleId);
    }

    public boolean deleteGpsData(String id) {
        Optional<Gps> existingGps = gpsDao.findById(id);
        if (existingGps.isPresent()) {
            gpsDao.deleteById(id);
            return true;
        }
        return false;
    }
}
