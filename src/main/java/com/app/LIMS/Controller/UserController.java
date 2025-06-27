package com.app.LIMS.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.Repository.UserRepository;
import com.app.LIMS.Services.UserService;
import com.app.LIMS.entity.User;
import com.app.LIMS.entity.UserRequest;
import com.app.LIMS.master.entity.Lab;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepo;
    
    @GetMapping
    public String  welcome() {
    return "hello";	
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUserType().equalsIgnoreCase("user")) {
        	
        	
        	
        
        	
        	return userService.authenticate(loginRequest.getUsername(),
            		
            		
            		loginRequest.getPassword(), loginRequest.getUserType())
                    .map(user -> {
                        User u = (User) user;
                        return ResponseEntity.ok(new UserDTO(u.getId(), u.getUsername(), u.getRole(), u.getEmail(), u.getLabcode()));
                    })
                    .orElse(ResponseEntity.status(401).build());
        } else {
            return userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getUserType())
                    .map(lab -> {
                        Lab l = (Lab) lab;  // ✅ cast to Lab entity
                        return ResponseEntity.ok(new LabDtO(l.getId(), l.getLabCode(), "lab", l.getStatus(), l.getLabCode()));
                    })
                    .orElse(ResponseEntity.status(401).build());
        }
    }

    

    @GetMapping("/users-master/{labCode}")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable String labCode) {
        List<User> users = userRepo.findByLabcode(labCode);
        List<User> dtos = users.stream()
            .map(u -> new User(u.getId(),u.getUsername(),u.getRole(),u.getEmail()))
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
  
   @PostMapping("/add")
    public  ResponseEntity<Map<String, Object>> addUser(@RequestBody UserRequest userRequest) {
	   Map<String, Object> response = new HashMap<>();
		boolean exists=userRepo.existsByUsernameAndLabcode(userRequest.getUsername(),userRequest.getLabcode());
        
    	if(exists) {
    		
    		 response.put("success", false);
             response.put("message", "User is already exists.");
             return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}
    	else {
    		 try {
    	            User createdUser = userService.addUser(userRequest);
    	            return ResponseEntity.status(HttpStatus.OK).body(response);
    	           // return ResponseEntity.ok(createdUser);
    	        } catch (Exception e) {
    	            return (ResponseEntity<Map<String, Object>>) ResponseEntity.badRequest();
    	        }	
    	}
       
    }
   
   
   
   
   
   
   
   
   // Edit user (User Master)
   @PostMapping("/user-master/update")
   public ResponseEntity<?> updateUserMaster(@RequestBody UserRequest userRequest) {
       try {
           User updatedUser = userService.updateUser(userRequest);
           return ResponseEntity.ok(new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getRole(), updatedUser.getEmail(), updatedUser.getLabcode()));
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }

   // Delete user (User Master)
   @DeleteMapping("/user-master/delete")
   public ResponseEntity<?> deleteUserMaster(@RequestParam Integer userId) {
       try {
           userService.deleteUser(userId);
           return ResponseEntity.ok("User deleted successfully");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }

   // (Optional) Track user activity
   @GetMapping("/user-master/activity")
   public ResponseEntity<List<String>> getUserActivity(@RequestParam Integer userId) {
       List<String> activity = userService.getUserActivity(userId); // Implement this in your service
       return ResponseEntity.ok(activity);
   }
    static class LoginRequest {
        private String username;
        private String password;
        private String userType;
        private String labcode;
        public String getLabcode() {
			return labcode;
		}
		public void setLabcode(String labcode) {
			this.labcode = labcode;
		}
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
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
        private String labCode;

        
        
        
        
        public UserDTO(Integer id, String username, String role, String email,String labCode) {
            this.id = id;
            this.username = username;
            this.role = role;
            this.email = email;
            this.setLabCode(labCode);
        }





		public String getLabCode() {
			return labCode;
		}





		public void setLabCode(String labCode) {
			this.labCode = labCode;
		}
        

     
    }
    
    	static class LabDtO {
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
        private String labCode;

        
        
        
        
        public LabDtO(Long long1, String username, String role, String email,String labCode) {
            this.id = id;
            this.username = username;
            this.role = role;
            this.email = email;
            this.labCode=labCode;
        }
        

     
    }
}