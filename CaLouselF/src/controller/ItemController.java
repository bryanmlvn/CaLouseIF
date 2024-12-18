package controller;

import repository.ItemRepository;
import java.sql.*;

public class ItemController {

    private ItemRepository itemRepository;
    
 

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void uploadItem(String name, String category, String size, String price, String sellerId) {
        itemRepository.uploadItem(name, category, size, price, sellerId);
    }

    public void editItem(String itemId, String name, String category, String size, String price) {
        itemRepository.updateItem(itemId, name, category, size, price);
    }

    public void deleteItem(String itemId) {
        itemRepository.deleteItem(itemId);
    }
    

    public ResultSet viewItemsBySellerId(String sellerId) {
        return itemRepository.viewItemsBySellerId(sellerId);
    }
    public ResultSet viewItemsForBuyers() {
        return itemRepository.viewItemsForBuyers(); // Use the instance of ItemRepository
    }
    public ResultSet viewItems() {
        return itemRepository.viewItems();
    }
    public ResultSet viewHistory(String userId) {
    	return itemRepository.viewHistory(userId);
    }
    public void purchaseItem(String itemId, String userId) {
    	itemRepository.purchaseItem(itemId, userId);
    }
    public void updateOfferPrice(String itemId, double newOfferPrice, String offerrorId) {
        itemRepository.updateOfferPrice(itemId, newOfferPrice, offerrorId);
    }
    public boolean validateFormSeller(String name, String category, String size, String price) {
    	if (name.isEmpty() && category.isEmpty() && size.isEmpty() && price.isEmpty()) {
            // Optionally set the color of the error message
            return false; // Do nothing if all fields are empty
        }
    	return true;
    }
    public void approveItem(String itemId) {
        itemRepository.updateItemStatus(itemId, "Approved");
    }
    public void declineItem(String itemId, String reason) {
        itemRepository.declineItem(itemId, reason);
    }
    public void acceptOffer(String itemId) {
    	itemRepository.acceptOffer(itemId);
    }
    public void declineOffer(String itemId, String reason) {
    	itemRepository.declineItem(itemId, reason);
    }
}