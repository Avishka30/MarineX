package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
