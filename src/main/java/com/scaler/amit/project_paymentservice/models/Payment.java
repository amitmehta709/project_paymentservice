package com.scaler.amit.project_paymentservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment extends BaseModel{
    private String orderId; // Razorpay order ID (e.g., "order_123")
    private String paymentId; // Razorpay payment ID (e.g., "pay_123")
    private String refundId; //Razorpay refund ID
    private String status;
    private Double amount;
    private String currency;
    private String invoiceNumber;
}
