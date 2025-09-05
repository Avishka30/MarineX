package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Booking;
import lk.ijse.gdse.backend.entity.Berth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings of a specific agent
    List<Booking> findByAgentUserId(Long userId);

    // Check overlapping bookings for a specific berth
    List<Booking> findByBerthBerthIdAndBookingDateLessThanEqualAndEndDateGreaterThanEqual(
            Long berthId,
            LocalDateTime endDate,
            LocalDateTime bookingDate
    );

    // Custom query to get all available berths for a given time range
    @Query("SELECT b FROM Berth b WHERE b.status = 'AVAILABLE' AND b.berthId NOT IN " +
            "(SELECT bo.berth.berthId FROM Booking bo WHERE bo.bookingDate <= :endDate AND bo.endDate >= :startDate)")
    List<Berth> findAvailableBerths(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
SELECT (COUNT(b) > 0) 
FROM Booking b
WHERE b.berth.berthId = :berthId
  AND b.bookingDate <= :endDate
  AND b.endDate   >= :startDate
""")
    boolean existsByBerthAndDateRange(
            @Param("berthId") Long berthId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate")   java.time.LocalDateTime endDate
    );


}
