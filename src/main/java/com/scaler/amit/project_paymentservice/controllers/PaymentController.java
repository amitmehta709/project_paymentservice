package com.scaler.amit.project_paymentservice.controllers;

import com.razorpay.RazorpayException;
import com.scaler.amit.project_paymentservice.dtos.PaymentCallbackRequest;
import com.scaler.amit.project_paymentservice.dtos.PaymentDto;
import com.scaler.amit.project_paymentservice.exceptions.InvalidRefundException;
import com.scaler.amit.project_paymentservice.exceptions.NotFoundException;
import com.scaler.amit.project_paymentservice.models.Payment;
import com.scaler.amit.project_paymentservice.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-paymentLink")
    public ResponseEntity<PaymentDto> createPaymentLink(@RequestParam Double amount,
                                                        @RequestParam String currency,
                                                        @RequestParam String invoiceNo) throws RazorpayException {

        PaymentDto paymentDto= paymentService.createPaymentLink(amount,currency,invoiceNo);
        return new ResponseEntity<>(paymentDto, HttpStatus.CREATED);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentDetails(@PathVariable String orderId) throws NotFoundException {
        Payment payment = paymentService.getPaymentDetails(orderId);
        return new ResponseEntity<>(PaymentDto.from(payment), HttpStatus.OK);
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<String> getPaymentStatus(@PathVariable String paymentId) throws RazorpayException {
        String response = paymentService.fetchPaymentStatus_Razorpay(paymentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create-refund/{orderId}")
    public ResponseEntity<PaymentDto> createRefund(@PathVariable String orderId) throws NotFoundException, InvalidRefundException, RazorpayException {
        Payment payment = paymentService.createRefund(orderId);
        return new ResponseEntity<>(PaymentDto.from(payment), HttpStatus.CREATED);
    }

    @GetMapping("/status/refund/{refundId}")
    public ResponseEntity<String> getRefundStatus(@PathVariable String refundId) throws RazorpayException {
        String response = paymentService.fetchRefundStatus(refundId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/callback")
    public String handlePaymentCallback(
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_payment_link_id") String paymentLinkId,
            @RequestParam("razorpay_payment_link_reference_id") String orderId,
            @RequestParam("razorpay_payment_link_status") String status,
            @RequestParam("razorpay_signature") String signature) throws RazorpayException, NotFoundException {

        PaymentCallbackRequest paymentCallbackRequest = new PaymentCallbackRequest();
        paymentCallbackRequest.setRazorpay_payment_id(paymentId);
        paymentCallbackRequest.setRazorpay_payment_link_id(paymentLinkId);
        paymentCallbackRequest.setRazorpay_payment_link_reference_id(orderId);
        paymentCallbackRequest.setRazorpay_payment_link_status(status);
        paymentCallbackRequest.setRazorpay_signature(signature);

        if(paymentService.verifySignature(paymentCallbackRequest))
        {
            paymentService.updatePaymentDetails(paymentCallbackRequest);
        }
        else {
            return "Signature verification failed";
        }

        return "paid".equals(status)?"Payment Successful !":"Payment Failed !";
    }

}
