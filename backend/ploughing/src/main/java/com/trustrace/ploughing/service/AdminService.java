package com.trustrace.ploughing.service;

import com.trustrace.ploughing.model.people.Admin;
import com.trustrace.ploughing.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    // Create or update an admin
    public Admin createAdmin(Admin admin) {
        return adminDao.save(admin);
    }

    // Get all admins
    public List<Admin> getAllAdmins() {
        return adminDao.findAll();
    }

    // Get an admin by ID
    public Optional<Admin> getAdminById(String id) {
        return adminDao.findById(id);
    }

    // Get an admin by email
    public Optional<Admin> getAdminByEmail(String email) {
        return adminDao.findByEmail(email);
    }

    // Delete an admin by ID
    public boolean deleteAdmin(String id) {
        Optional<Admin> existingAdmin = adminDao.findById(id);
        if (existingAdmin.isPresent()) {
            adminDao.deleteById(id);
            return true;
        }
        return false;
    }
}
