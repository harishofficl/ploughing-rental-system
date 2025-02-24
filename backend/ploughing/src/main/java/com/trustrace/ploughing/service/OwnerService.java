package com.trustrace.ploughing.service;

import com.trustrace.ploughing.dao.OwnerDao;
import com.trustrace.ploughing.model.people.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private OwnerDao ownerDao;

    // Create or update an owner
    public Owner createOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    // Get all owners
    public List<Owner> getAllOwners() {
        return ownerDao.findAll();
    }

    // Get an owner by ID
    public Optional<Owner> getOwnerById(String id) {
        return ownerDao.findById(id);
    }

    // Get an owner by email
    public Optional<Owner> getOwnerByEmail(String email) {
        return ownerDao.findByEmail(email);
    }

    // Delete an owner by ID
    public boolean deleteOwner(String id) {
        Optional<Owner> existingOwner = ownerDao.findById(id);
        if (existingOwner.isPresent()) {
            ownerDao.deleteById(id);
            return true;
        }
        return false;
    }
}
