package lk.ijse.gdse.backend.dto;

import lk.ijse.gdse.backend.entity.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public class BookingDTO {
    private Long bookingId;
    private Long vesselId;
    private Long berthId;
    private Long agentId;
    private List<Long> serviceIds;
    private LocalDateTime bookingDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private BookingStatus status;

    public BookingDTO() {}

    public BookingDTO(Long bookingId, Long vesselId, Long berthId, Long agentId,
                      List<Long> serviceIds, LocalDateTime bookingDate, LocalDateTime endDate,
                      Double totalPrice, BookingStatus status) {
        this.bookingId = bookingId;
        this.vesselId = vesselId;
        this.berthId = berthId;
        this.agentId = agentId;
        this.serviceIds = serviceIds;
        this.bookingDate = bookingDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters & Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getVesselId() { return vesselId; }
    public void setVesselId(Long vesselId) { this.vesselId = vesselId; }

    public Long getBerthId() { return berthId; }
    public void setBerthId(Long berthId) { this.berthId = berthId; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    public List<Long> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
