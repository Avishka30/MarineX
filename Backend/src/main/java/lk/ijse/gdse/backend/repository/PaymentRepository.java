package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingAgentUserId(Long agentId);

}
