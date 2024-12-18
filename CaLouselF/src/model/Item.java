package model;

public class Item {
//	-Item_id: String
//	-Item name: String
//	-Item_size: String
//	-Item_price: String
//	- Item_category : String
//	-item_ status: String
//	-item_wishlist : String
//	-Item offer status: String
	private String id;
	private String name;
	private String size;
	private Double price;
	private String category;
	private String status;
	private String wishlist;
	private String offerStatus;
	private String sellerId;
	private Double offerPrice;
	private String statusReason;
	private String lastOfferror;
	public Item(String id, String name, String size, Double price, String category, String status, String wishlist,
			String offerStatus, String sellerId, Double offerPrice, String statusReason, String lastOfferror) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
		this.price = price;
		this.category = category;
		this.status = status;
		this.wishlist = wishlist;
		this.offerStatus = offerStatus;
		this.sellerId =  sellerId;
		this.offerPrice = offerPrice;
		this.statusReason = statusReason;
		this.lastOfferror = lastOfferror;
	}
	public String getStatusReason() {
		return statusReason;
	}
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public Double getOfferPrice() {
		return offerPrice;
	}
	public void setOfferPrice(Double offerPrice) {
		this.offerPrice = offerPrice;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Double getPrice() {
		return price;
	}
	public void Double(Double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWishlist() {
		return wishlist;
	}
	public void setWishlist(String wishlist) {
		this.wishlist = wishlist;
	}
	public String getOfferStatus() {
		return offerStatus;
	}
	public void setOfferStatus(String offerStatus) {
		this.offerStatus = offerStatus;
	}
	
	
}
