package com.example.ims.dto;

import java.math.BigDecimal;

public class CartItemDto {
	 private Long productId;
     private String productName;
     private BigDecimal price;
     private Integer quantity;
     
	public CartItemDto() {
		super();
	}
	public CartItemDto(Long productId, String productName, BigDecimal price, Integer quantity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "CartItemDto [productId=" + productId + ", productName=" + productName + ", quantity=" + quantity + "]";
	}
	
     
}
