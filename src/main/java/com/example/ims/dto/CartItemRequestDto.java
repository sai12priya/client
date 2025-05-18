package com.example.ims.dto;

public class CartItemRequestDto {
  
    private int userId;      // For logged-in users
    private Long productId;
    private int quantity;
    
    
    public CartItemRequestDto() {
		super();
	}
	public CartItemRequestDto( int userId, Long productId, int quantity) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}
	// getters/setters
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "CartItemRequest ["+ " userId=" + userId + ", productId=" + productId
				+ ", quantity=" + quantity + "]";
	}
    
}
