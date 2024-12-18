package controller;

import java.sql.ResultSet;

import repository.WishlistRepository;

public class WishlistController {
    private WishlistRepository wishlistRepository;

    public WishlistController(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void addItemToWishlist(String itemId, String userId) {
        wishlistRepository.addItemToWishlist(itemId, userId);
    }
    public ResultSet viewWishlist(String userId) {
    	return wishlistRepository.viewWishlist(userId);
    }
    public void removeItemFromWishlist(String userId, String itemId) {
        try {
            wishlistRepository.removeItemFromWishlist(userId, itemId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to remove item from wishlist.");
        }
    }
}