package com.app.LIMS.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.LIMS.Repository.UserRepository;
import com.app.LIMS.entity.User;
import com.app.LIMS.entity.UserRequest;
import com.app.LIMS.master.Repository.LabRepository;
import com.app.LIMS.master.entity.Lab;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private LabRepository labRepo;


    public Optional<?> authenticate(String username, String rawPassword,String userType) {
    	  Optional<User> userOpt = java.util.Optional.empty();
    	  Optional<Lab> labOpt;
    	 if ("user".equalsIgnoreCase(userType)) {
    		 userOpt= userRepository.findByUsername(username);
    		   if (userOpt.isPresent()) {
    	            User user = userOpt.get();
    	            System.out.println("Raw password: " + rawPassword);
    	            System.out.println("DB hash: " + user.getPasswordHash());
    	            boolean matches = passwordEncoder.matches(rawPassword, user.getPasswordHash());
    	            System.out.println("Password matches: " + matches);
    	            if (matches) {
    	                return Optional.of(user);
    	            }
    	        }
    	 }
    	 else {
    		 labOpt=labRepo.findByLabCode(username);
    		 
    		   if (labOpt.isPresent()) {
    	            Lab lab = labOpt.get();
    	            System.out.println("Raw password: " + rawPassword);
    	            System.out.println("DB hash: " + lab.getPassword());
    	            boolean matches = passwordEncoder.matches(rawPassword, lab.getPassword());
    	            System.out.println("Password matches: " + matches);
    	            if (matches) {
    	                return Optional.of(lab);
    	            }
    	        }
    	 }
     
        return Optional.empty();
    }
    
    
    public User addUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setLabcode(userRequest.getLabcode());
        user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
        
        return userRepository.save(user);
    }
    
    
    // Update user (User Master)
    public User updateUser(UserRequest userRequest) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByIdAndLabcode(userRequest.getId(),userRequest.getLabcode()));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setRole(userRequest.getRole());
            user.setLabcode(userRequest.getLabcode());
            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Delete user (User Master)
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    // (Optional) Track user activity (stub implementation)
    public List<String> getUserActivity(Integer userId) {
        // Replace this with actual activity tracking logic
        List<String> activity = new ArrayList<>();
        activity.add("Logged in at 2025-06-19 10:00");
        activity.add("Edited profile at 2025-06-19 10:15");
        activity.add("Logged out at 2025-06-19 10:30");
        return activity;
    }

    
}