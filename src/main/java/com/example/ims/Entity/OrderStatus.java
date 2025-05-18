package com.example.ims.Entity;
//src/main/java/com/yourpackage/model/enums/OrderStatus.java
public enum OrderStatus {
 PENDING,       // Order placed but payment pending
 PAID,          // Payment confirmed
 SHIPPED,       // Items dispatched
 DELIVERED,     // Customer received
 CANCELLED,     // Order cancelled pre-delivery
 REFUNDED       // Payment returned
}