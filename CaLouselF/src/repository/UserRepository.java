package repository;

import util.Connect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserRepository {
    public boolean saveUser(User user) {
        String query = "INSERT INTO User (User_id, Username, Password, Phone_Number, Address, Role) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = Connect.getInstance().preparedStatement(query);
            ps.setString(1, user.getUserId());         // User ID
            ps.setString(2, user.getUsername());      // Username
            ps.setString(3, user.getPassword());      // Password
            ps.setString(4, user.getPhoneNumber());   // Phone Number
            ps.setString(5, user.getAddress());       // Address
            ps.setString(6, user.getRole());          // Role
            ps.executeUpdate();

            return true; // Save operation successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Save operation failed
        }
    }
    public User loginUser(String username, String password) {
        String query = "SELECT * FROM User WHERE Username = ?";

        try {
            PreparedStatement ps = Connect.getInstance().preparedStatement(query);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            // Check if user exists
            if (rs.next()) {
                // Retrieve the user details
                String storedPassword = rs.getString("Password");

                // If the password matches, return the user object
                if (storedPassword.equals(password)) {
                    String userId = rs.getString("User_id");
                    String phoneNumber = rs.getString("Phone_Number");
                    String address = rs.getString("Address");
                    String role = rs.getString("Role");

                    User user = new User(userId, username, storedPassword, phoneNumber, address, role);
                    return user; // User is authenticated, return the User object
                } else {
                    return null; // Password doesn't match
                }
            } else {
                return null; // No user found with the given username
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during the query
        }
    }
}