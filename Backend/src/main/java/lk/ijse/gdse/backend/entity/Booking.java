package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vessel_id", nullable = false)
    private Vessel vessel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id", nullable = false)
    private Berth berth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    private LocalDateTime bookingDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private BookingPurpose purpose; // Enum instead of String

    private String cargoCategory;
    private Double cargoQuantity;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingService> bookingServices;

    // New field for total price
    private Double totalPrice;

    // Constructors
    public Booking() {}

    public Booking(Long bookingId, Vessel vessel, Berth berth, User agent, LocalDateTime bookingDate,
                   LocalDateTime endDate, BookingPurpose purpose, String cargoCategory, Double cargoQuantity,
                   BookingStatus status, LocalDateTime createdAt, List<BookingService> bookingServices,
                   Double totalPrice) {
        this.bookingId = bookingId;
        this.vessel = vessel;
        this.berth = berth;
        this.agent = agent;
        this.bookingDate = bookingDate;
        this.endDate = endDate;
        this.purpose = purpose;
        this.cargoCategory = cargoCategory;
        this.cargoQuantity = cargoQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.bookingServices = bookingServices;
        this.totalPrice = totalPrice;
    }

    // Getters & Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Vessel getVessel() { return vessel; }
    public void setVessel(Vessel vessel) { this.vessel = vessel; }

    public Berth getBerth() { return berth; }
    public void setBerth(Berth berth) { this.berth = berth; }

    public User getAgent() { return agent; }
    public void setAgent(User agent) { this.agent = agent; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public BookingPurpose getPurpose() { return purpose; }
    public void setPurpose(BookingPurpose purpose) { this.purpose = purpose; }

    public String getCargoCategory() { return cargoCategory; }
    public void setCargoCategory(String cargoCategory) { this.cargoCategory = cargoCategory; }

    public Double getCargoQuantity() { return cargoQuantity; }
    public void setCargoQuantity(Double cargoQuantity) { this.cargoQuantity = cargoQuantity; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<BookingService> getBookingServices() { return bookingServices; }
    public void setBookingServices(List<BookingService> bookingServices) { this.bookingServices = bookingServices; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
