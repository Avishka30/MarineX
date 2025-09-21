package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.PaymentDTO;
import lk.ijse.gdse.backend.entity.Booking;
import lk.ijse.gdse.backend.entity.BookingStatus;
import lk.ijse.gdse.backend.entity.Payment;
import lk.ijse.gdse.backend.repository.BookingRepository;
import lk.ijse.gdse.backend.repository.PaymentRepository;
import lk.ijse.gdse.backend.service.EmailService;
import lk.ijse.gdse.backend.service.InvoiceGenerator;
import lk.ijse.gdse.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    @Autowired
    private EmailService emailService;

    @Override
    public PaymentDTO makePayment(PaymentDTO paymentDTO) {
        // Find booking
        Booking booking = bookingRepository.findById(paymentDTO.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Create payment entity
        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setMethod(paymentDTO.getMethod());
        payment.setStatus("SUCCESS"); // default for now
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        payment.setBooking(booking);

        // Save payment
        Payment saved = paymentRepository.save(payment);

        // Generate invoice PDF
        byte[] pdfBytes = invoiceGenerator.generateInvoicePdf(booking, saved);

        // Send email to agent
        String agentEmail = booking.getAgent().getEmail();
        if (agentEmail != null && !agentEmail.isEmpty()) {
            emailService.sendEmailWithAttachment(
                    agentEmail,
                    "Payment Receipt - Booking #" + booking.getBookingId(),
                    "Dear Agent,\n\nYour payment was successful. Please find the invoice attached.",
                    pdfBytes,
                    "Invoice_Booking_" + booking.getBookingId() + ".pdf"
            );
        }

        // ✅ Update booking status to CONFIRMED
        booking.setStatus(BookingStatus.valueOf("CONFIRMED"));
        bookingRepository.save(booking);

        // Return DTO
        PaymentDTO dto = new PaymentDTO();
        dto.setId(saved.getId());
        dto.setAmount(saved.getAmount());
        dto.setMethod(saved.getMethod());
        dto.setStatus(saved.getStatus());
        dto.setPaymentDate(saved.getPaymentDate());
        dto.setTransactionId(saved.getTransactionId());
        dto.setBookingId(booking.getBookingId());

        return dto;
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return mapToDTO(payment);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setTransactionId(payment.getTransactionId());
        dto.setBookingId(payment.getBooking().getBookingId());
        return dto;
    }

    public List<PaymentDTO> getPaymentsByAgent(Long agentId) {
        List<Payment> payments = paymentRepository.findByBookingAgentUserId(agentId);
        return payments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



}
