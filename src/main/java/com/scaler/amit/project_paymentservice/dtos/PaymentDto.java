package com.scaler.amit.project_paymentservice.dtos;

import com.scaler.amit.project_paymentservice.models.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private String orderId; // Razorpay order ID (e.g., "order_123")
    private String paymentId; // Razorpay payment ID (e.g., "pay_123")
    private String refundId; //Razorpay refund ID
    private String status;
    private String paymentLink;

    public static PaymentDto from(Payment payment, String paymentLink) {
        PaymentDto paymentDto = from(payment);
        paymentDto.setPaymentLink(paymentLink);
        return paymentDto;
    }

    public static PaymentDto from(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setOrderId(payment.getOrderId());
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setRefundId(payment.getRefundId());
        paymentDto.setStatus(payment.getStatus());
        return paymentDto;
    }
}
