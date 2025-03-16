package com.scaler.amit.project_paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCallbackRequest {
    private String razorpay_payment_id;
    private String razorpay_payment_link_id;
    private String razorpay_payment_link_reference_id;
    private String razorpay_payment_link_status;
    private String razorpay_signature;
}
