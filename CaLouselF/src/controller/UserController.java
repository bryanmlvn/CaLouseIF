package controller;

import model.User;
import repository.UserRepository;

import java.util.concurrent.ThreadLocalRandom;

public class UserController {
    private final UserRepository userRepository = new UserRepository();

    // Generate unique User_id
    public String generateUserId() {
        return "USER" + ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    // Validate input fields
    private boolean validateRegisterForm(String username, String password, String phoneNumber, String address, String role, boolean termsAgreed) {
        if (username == null || username.isEmpty() || 
            password == null || password.isEmpty() || 
            phoneNumber == null || phoneNumber.isEmpty() || 
            address == null || address.isEmpty() || 
            role == null || !termsAgreed) {
            return false;
        }
        return true;
    }

    // Register user and save to the database
    public String registerUser(String username, String password, String phoneNumber, String address, String role, boolean termsAgreed) {
        // Validate inputs
        if (!validateRegisterForm(username, password, phoneNumber, address, role, termsAgreed)) {
            return "Please fill out all fields and agree to the terms.";
        }

        // Generate User ID and save the user
        String userId = generateUserId();
        User user = new User(userId, username, password, phoneNumber, address, role);

        boolean isSaved = userRepository.saveUser(user);
        return isSaved ? "Registration successful!" : "Registration failed. Please try again.";
    }
    
    public User loginUser(String username, String password) {
        // Validate the login form
        if (!validateLoginForm(username, password)) {
            return null; // Indicate invalid form input
        }

        // Proceed to retrieve the user from the repository
        return userRepository.loginUser(username, password);
    }

    private boolean validateLoginForm(String username, String password) {
        // Check if username or password are null or empty (ignoring whitespace)
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
}