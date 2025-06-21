package com.app.LIMS.Services;

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


	 	@Value("${labCode}")
	    private String labCode;
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
    
    
}