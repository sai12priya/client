package com.example.ims.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Purchase {

	    private Long id;
	    
	    private Vendor vendor;
	    
	    private Double price;
	    
	    private Integer quantityAvailable;
	  
	    private Double totalAmount;
	    
	    private LocalDate purchaseDate;
	    
	    private LocalDate expectedDeliveryDate;
	    
	    
	    public Purchase() {}
	    
	    
	    public Purchase( Vendor vendor,  Double totalAmount, Double price,Integer quantity,
				LocalDate purchaseDate, LocalDate expectedDeliveryDate) {
			super();
			//this.id = id;
			this.vendor = vendor;
			this.price=price;
			this.quantityAvailable=quantity;
			this.totalAmount = totalAmount;
			this.purchaseDate = purchaseDate;
			this.expectedDeliveryDate = expectedDeliveryDate;
			
		}


		public Double getPrice() {
			return price;
		}


		public void setPrice(Double price) {
			this.price = price;
		}


		public Integer getQuantityAvailable() {
			return quantityAvailable;
		}


		public void setQuantityAvailable(Integer quantityAvailable) {
			this.quantityAvailable = quantityAvailable;
		}


		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Vendor getVendor() {
			return vendor;
		}
		public void setVendor(Vendor vendor) {
			this.vendor = vendor;
		}
		
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		public LocalDate getPurchaseDate() {
			return purchaseDate;
		}
		public void setPurchaseDate(LocalDate purchaseDate) {
			this.purchaseDate = purchaseDate;
		}
		public LocalDate getExpectedDeliveryDate() {
			return expectedDeliveryDate;
		}
		public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
			this.expectedDeliveryDate = expectedDeliveryDate;
		}
		

		@Override
		public String toString() {
			return "Purchase [id=" + id + ", vendor=" + vendor +", totalAmount=" + totalAmount + ", purchaseDate=" + purchaseDate
					+ ", expectedDeliveryDate=" + expectedDeliveryDate + "]";
		}
	    
	    

}