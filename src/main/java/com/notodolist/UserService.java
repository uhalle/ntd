package com.notodolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	   @Autowired
	    private UserRepository userRepository;
	
	public String registerUser(SignupRequest signUpDetails) {
		
		User user = setUser(signUpDetails);

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already exists!";
        }

        // Save user
        userRepository.save(user);

        return "User registered successfully!";
    }

	private User setUser(SignupRequest signUpUser) {
		User user = new User();
		user.setUsername(signUpUser.getName());
		user.setEmail(signUpUser.getEmail());
		user.setPassword(signUpUser.getPassword());

		return user;
	}
	
}
