package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.WorkerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerAssignmentRepository extends JpaRepository<WorkerAssignment, Long> {
    // Optional: fetch assignments by bookingService id
    List<WorkerAssignment> findByBookingServiceBookingServiceId(Long bookingServiceId);
}
