package com.trustrace.ploughing.security.service;


import com.trustrace.ploughing.dao.AdminDao;
import com.trustrace.ploughing.dao.DriverDao;
import com.trustrace.ploughing.dao.OwnerDao;
import com.trustrace.ploughing.model.people.Admin;
import com.trustrace.ploughing.model.people.Driver;
import com.trustrace.ploughing.model.people.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private OwnerDao ownerDao;

    @Autowired
    private DriverDao driverDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (adminDao.findByEmail(email).isPresent()) {
            Admin admin = adminDao.findByEmail(email).get();
            return new User(admin.getEmail(), admin.getPassword(), mapRolesToAuthorities(admin.getRoles()));
        } else if (ownerDao.findByEmail(email).isPresent()) {
            Owner owner = ownerDao.findByEmail(email).get();
            return new User(owner.getEmail(), owner.getPassword(), mapRolesToAuthorities(owner.getRoles()));
        } else if (driverDao.findByEmail(email).isPresent()) {
            Driver driver = driverDao.findByEmail(email).get();
            return new User(driver.getEmail(), driver.getPassword(), mapRolesToAuthorities(driver.getRoles()));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
