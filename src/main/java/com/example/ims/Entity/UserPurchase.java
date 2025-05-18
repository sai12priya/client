package com.example.ims.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserPurchase {
    private Long id;
    private String invoiceNumber;
    private User user;
    private List<PurchaseItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.PENDING;
    private BigDecimal subtotal;
    private BigDecimal shipping;
    private BigDecimal tax;
    private BigDecimal total;
    private String paymentMethod;
    private ShippingAddress shippingAddress;
    private LocalDateTime purchaseDate = LocalDateTime.now();

    public UserPurchase() {
    }

    public UserPurchase(Long id, String invoiceNumber, User user, List<PurchaseItem> items, 
                      OrderStatus status, BigDecimal subtotal, BigDecimal shipping, 
                      BigDecimal tax, BigDecimal total, String paymentMethod,
                      ShippingAddress shippingAddress, LocalDateTime purchaseDate) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.user = user;
        this.items = items;
        this.status = status;
        this.subtotal = subtotal;
        this.shipping = shipping;
        this.tax = tax;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.purchaseDate = purchaseDate;
    }

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getInvoiceNumber() {
	return invoiceNumber;
}

public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}

public List<PurchaseItem> getItems() {
	return items;
}

public void setItems(List<PurchaseItem> items) {
	this.items = items;
}

public OrderStatus getStatus() {
	return status;
}

public void setStatus(OrderStatus status) {
	this.status = status;
}

public BigDecimal getSubtotal() {
	return subtotal;
}

public void setSubtotal(BigDecimal subtotal) {
	this.subtotal = subtotal;
}

public BigDecimal getShipping() {
	return shipping;
}

public void setShipping(BigDecimal shipping) {
	this.shipping = shipping;
}

public BigDecimal getTax() {
	return tax;
}

public void setTax(BigDecimal tax) {
	this.tax = tax;
}

public BigDecimal getTotal() {
	return total;
}

public void setTotal(BigDecimal total) {
	this.total = total;
}

public String getPaymentMethod() {
	return paymentMethod;
}

public void setPaymentMethod(String paymentMethod) {
	this.paymentMethod = paymentMethod;
}

public ShippingAddress getShippingAddress() {
	return shippingAddress;
}

public void setShippingAddress(ShippingAddress shippingAddress) {
	this.shippingAddress = shippingAddress;
}

public LocalDateTime getPurchaseDate() {
	return purchaseDate;
}

public void setPurchaseDate(LocalDateTime purchaseDate) {
	this.purchaseDate = purchaseDate;
}

@Override
public String toString() {
	return "UserPurchase [id=" + id + ", invoiceNumber=" + invoiceNumber + ", user=" + user + ", items=" + items
			+ ", status=" + status + ", subtotal=" + subtotal + ", shipping=" + shipping + ", tax=" + tax + ", total="
			+ total + ", paymentMethod=" + paymentMethod + ", shippingAddress=" + shippingAddress + ", purchaseDate="
			+ purchaseDate + "]";
}


 // Generate getters and setters
}