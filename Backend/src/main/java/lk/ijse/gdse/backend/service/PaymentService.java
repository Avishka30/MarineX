package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO makePayment(PaymentDTO paymentDTO);
}
