package com.trustrace.ploughing.security.service;

import com.trustrace.ploughing.dao.AdminDao;
import com.trustrace.ploughing.dao.DriverDao;
import com.trustrace.ploughing.dao.OwnerDao;
import com.trustrace.ploughing.model.people.Admin;
import com.trustrace.ploughing.model.people.Driver;
import com.trustrace.ploughing.model.people.Owner;
import com.trustrace.ploughing.security.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FetchUserService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private OwnerDao ownerDao;

    @Autowired
    private DriverDao driverDao;

    public UserDetail loadUserByEmail(String email) throws UsernameNotFoundException {
        if (adminDao.findByEmail(email).isPresent()) {
            Admin admin = adminDao.findByEmail(email).get();
            return new UserDetail(admin.getId(), admin.getName(), "admin");
        } else if (ownerDao.findByEmail(email).isPresent()) {
            Owner owner = ownerDao.findByEmail(email).get();
            return new UserDetail(owner.getId(), owner.getName(), "owner");
        } else if (driverDao.findByEmail(email).isPresent()) {
            Driver driver = driverDao.findByEmail(email).get();
            return new UserDetail(driver.getId(), driver.getName(), "driver");
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
