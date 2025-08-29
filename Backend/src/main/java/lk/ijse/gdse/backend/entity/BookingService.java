package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "booking_service")
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingServiceId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    // Constructors
    public BookingService() {}
    public BookingService(Long bookingServiceId, Booking booking, Services service) {
        this.bookingServiceId = bookingServiceId;
        this.booking = booking;
        this.service = service;
    }

    // Getters & Setters
    public Long getBookingServiceId() { return bookingServiceId; }
    public void setBookingServiceId(Long bookingServiceId) { this.bookingServiceId = bookingServiceId; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public Services getService() { return service; }
    public void setService(Services service) { this.service = service; }
}
