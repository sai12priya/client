package com.example.ims.dto;

import java.util.List;

public class PurchaseRequestDTO {
	 private Long userId;
	    private List<PurchaseItemDTO> items;
	    private ShippingAddressDTO shippingAddress;
	    private String paymentMethod;
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public List<PurchaseItemDTO> getItems() {
			return items;
		}
		public void setItems(List<PurchaseItemDTO> items) {
			this.items = items;
		}
		public ShippingAddressDTO getShippingAddress() {
			return shippingAddress;
		}
		public void setShippingAddress(ShippingAddressDTO shippingAddress) {
			this.shippingAddress = shippingAddress;
		}
		public String getPaymentMethod() {
			return paymentMethod;
		}
		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}
	    
	    
}
