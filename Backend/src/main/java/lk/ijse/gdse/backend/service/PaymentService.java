package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO makePayment(PaymentDTO paymentDTO);

    List<PaymentDTO> getAllPayments();

    PaymentDTO getPaymentById(Long id);

    List<PaymentDTO> getPaymentsByAgent(Long agentId);
}
