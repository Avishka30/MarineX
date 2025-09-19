package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingService, Long> {
    // You can add custom queries if needed later
}
