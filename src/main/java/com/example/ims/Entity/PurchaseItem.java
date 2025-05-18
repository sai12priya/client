package com.example.ims.Entity;

import java.math.BigDecimal;

public class PurchaseItem {
    private Long id;
    private UserPurchase userPurchase;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;

    public PurchaseItem() {
    }

    public PurchaseItem(Long id, UserPurchase userPurchase, Product product, 
                      int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.userPurchase = userPurchase;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserPurchase getUserPurchase() {
        return userPurchase;
    }

    public void setUserPurchase(UserPurchase userPurchase) {
        this.userPurchase = userPurchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "PurchaseItem [id=" + id + ", userPurchase=" + userPurchase 
             + ", product=" + product + ", quantity=" + quantity
             + ", unitPrice=" + unitPrice + "]";
    }

    // Business logic methods
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}