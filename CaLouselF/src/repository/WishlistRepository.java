package repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Connect;

public class WishlistRepository {
	private Connect connect = Connect.getInstance();
	
	public void addItemToWishlist(String itemId, String userId) {
	    // Generate a random 4-digit number for the Wishlist_id
	    int randomNumber = (int) (Math.random() * 9000) + 1000; // Generates a number between 1000 and 9999
	    String wishlistId = "WL" + randomNumber; // Concatenate "WL" with the random number

	    // SQL query to insert the new wishlist entry
	    String query = "INSERT INTO Wishlist (Wishlist_id, Item_id, User_id) VALUES (?, ?, ?)";

	    try (PreparedStatement ps = connect.preparedStatement(query)) {
	        ps.setString(1, wishlistId); // Set Wishlist_id
	        ps.setString(2, itemId);     // Set Item_id
	        ps.setString(3, userId);     // Set User_id
	        ps.executeUpdate();          // Execute the update
	        System.out.println("Item successfully added to wishlist.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to add item to wishlist.");
	    }
	}
	public ResultSet viewWishlist(String userId) {
        String query = "SELECT I.* " +
                       "FROM Item I " +
                       "JOIN Wishlist W ON I.Item_id = W.Item_id " +
                       "WHERE W.User_id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = connect.preparedStatement(query);
            ps.setString(1, userId); // Set the userId parameter
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs; // Return the ResultSet containing the items
    }
	public void removeItemFromWishlist(String userId, String itemId) throws SQLException {
        String query = "DELETE FROM Wishlist WHERE User_id = ? AND Item_id = ?";
        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, userId);
            ps.setString(2, itemId);
            ps.executeUpdate();
        }
    }
	

}
