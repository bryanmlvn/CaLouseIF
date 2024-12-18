package repository;

import java.sql.*;

import util.Connect;

public class ItemRepository {

	private Connect connect = Connect.getInstance();

    public ItemRepository() {
    }

    public void uploadItem(String name, String category, String size, String price, String sellerId) {
        // Generate a random 4-digit number for the Item_id
        int randomNumber = (int) (Math.random() * 9000) + 1000;  // Generates a number between 1000 and 9999
        String itemId = "ITEM" + randomNumber;  // Concatenate "ITEM" with the random number

        String query = "INSERT INTO Item (Item_id, Item_name, Item_category, Item_size, Item_price, Item_status, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, itemId);
            ps.setString(2, name);
            ps.setString(3, category);
            ps.setString(4, size);
            ps.setString(5, price);
            ps.setString(6, "Not approved");
            ps.setString(7, sellerId);  // Insert seller_id
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(String itemId, String name, String category, String size, String price) {
        String query = "UPDATE Item SET Item_name = ?, Item_category = ?, Item_size = ?, Item_price = ? WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, size);
            ps.setString(4, price);
            ps.setString(5, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void purchaseItem(String itemId, String userId) {
        String query = "UPDATE Item SET Item_status = 'Sold', Item_buyer_id = ? WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, userId); // Set Item_buyer_id
            ps.setString(2, itemId); // Set Item_id in the WHERE clause
            ps.executeUpdate();
            System.out.println("Item with ID " + itemId + " has been marked as Sold.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOfferPrice(String itemId, double newOfferPrice, String offerrorId) {
        String query = "UPDATE Item SET Item_offer_price = ?, Item_last_offerror = ? WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setDouble(1, newOfferPrice); // Set the new offer price
            ps.setString(2, offerrorId);    // Set the offerror ID
            ps.setString(3, itemId);        // Identify the item to update
            ps.executeUpdate();
            System.out.println("Offer price and offerror updated for item with ID: " + itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteItem(String itemId) {
        String query = "DELETE FROM Item WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet viewItems() {
        String query = "SELECT * FROM Item";
        ResultSet rs = null;

        try {
            PreparedStatement ps = connect.preparedStatement(query);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs; // Return the ResultSet
    }
    public ResultSet viewHistory(String userId) {
        String query = "SELECT * FROM Item WHERE Item_buyer_id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = connect.preparedStatement(query);
            ps.setString(1, userId); // Set the userId as the parameter
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs; // Return the ResultSet containing the history of items bought by the user
    }

    public ResultSet viewItemsForBuyers() {
        String query = "SELECT * FROM Item WHERE Item_status = 'Approved'";
        ResultSet rs = null;

        try {
            PreparedStatement ps = connect.preparedStatement(query);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs; // Return the ResultSet
    }
    


    public ResultSet viewItemsBySellerId(String sellerId) {
        String query = "SELECT * FROM Item WHERE seller_id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = connect.preparedStatement(query);
            ps.setString(1, sellerId); // Set the sellerId parameter
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs; // Return the ResultSet
    }
    public void updateItemStatus(String itemId, String status) {
        String query = "UPDATE Item SET Item_status = ? WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, status);
            ps.setString(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void declineItem(String itemId, String reason) {
        String query = "UPDATE Item SET Item_status = ?, Item_status_reason = ? WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, "Declined");
            ps.setString(2, reason);
            ps.setString(3, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void acceptOffer(String itemId) {
        String query = "UPDATE Item " +
                       "SET Item_status = 'Sold', " +
                       "Item_buyer_id = Item_last_offerror, " +
                       "Item_offer_status = 'Accepted', " +
                       "Item_price = Item_offer_price " +  // Set the price to the offer price
                       "WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, itemId); // Set the item ID
            ps.executeUpdate();
            System.out.println("Offer accepted and item with ID " + itemId + " has been updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void declineOffer(String itemId, String reason) {
        String query = "UPDATE Item SET Item_offer_status = ?, Item_offer_reason = ?, Item_last_offerror = NULL, Item_offer_price = NULL WHERE Item_id = ?";

        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, "Declined");
            ps.setString(2, reason);
            ps.setString(3, itemId);
            ps.executeUpdate();
            System.out.println("Offer for item with ID " + itemId + " has been declined.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

}