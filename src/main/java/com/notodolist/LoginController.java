package com.notodolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signUpuser) {
        return userService.registerUser(signUpuser);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Find user by username
        User user = userRepository.findByUsername(loginRequest.getUsername());
    
        if (user == null) {
            return ResponseEntity.status(401).body("User not found1");
        }

        // Check password (in real apps, use hashed passwords!)
        if (!user.getPassword().equals(loginRequest.getPassword())) {   
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // If valid, return success (you can generate JWT here)
        return ResponseEntity.ok(new LoginResponse("Login successful", user.getId(), user.getUsername()));
    }
}