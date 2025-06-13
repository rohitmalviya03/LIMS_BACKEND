package com.app.LIMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.Repository.UserRepository;
import com.app.LIMS.Services.UserService;
import com.app.LIMS.entity.User;
import com.app.LIMS.entity.UserRequest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepo;
    

    
    
    
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest) {
        return userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())
                .map(user -> ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getRole(), user.getEmail())))
                .orElse(ResponseEntity.status(401).build());
    }

    
    

    @GetMapping("/users-master")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<User> dtos = users.stream()
            .map(u -> new User(u.getId(),u.getUsername(),u.getRole()))
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
  
   @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest) {
        try {
            User createdUser = userService.addUser(userRequest);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

  
    static class UserDTO {
        private Integer id;
        private String username;
        public Integer getId() {
			return id;
		}





		public void setId(Integer id) {
			this.id = id;
		}





		public String getUsername() {
			return username;
		}





		public void setUsername(String username) {
			this.username = username;
		}





		public String getRole() {
			return role;
		}





		public void setRole(String role) {
			this.role = role;
		}





		public String getEmail() {
			return email;
		}





		public void setEmail(String email) {
			this.email = email;
		}





		private String role;
        private String email;

        
        
        
        
        public UserDTO(Integer id, String username, String role, String email) {
            this.id = id;
            this.username = username;
            this.role = role;
            this.email = email;
        }
    }
}