
package lk.ijse.gdse.backend.dto;

import lk.ijse.gdse.backend.entity.BookingPurpose;
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

    // Missing fields from entity
    private BookingPurpose purpose;
    private String cargoCategory;
    private Double cargoQuantity;


    public BookingDTO() {}

    // Full constructor (all fields)
    public BookingDTO(Long bookingId, Long vesselId, Long berthId, Long agentId,
                      List<Long> serviceIds, LocalDateTime bookingDate, LocalDateTime endDate,
                      Double totalPrice, BookingStatus status, BookingPurpose purpose,
                      String cargoCategory, Double cargoQuantity, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.vesselId = vesselId;
        this.berthId = berthId;
        this.agentId = agentId;
        this.serviceIds = serviceIds;
        this.bookingDate = bookingDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.purpose = purpose;
        this.cargoCategory = cargoCategory;
        this.cargoQuantity = cargoQuantity;

    }

    // Getters and Setters for all fields
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

    public BookingPurpose getPurpose() { return purpose; }
    public void setPurpose(BookingPurpose purpose) { this.purpose = purpose; }

    public String getCargoCategory() { return cargoCategory; }
    public void setCargoCategory(String cargoCategory) { this.cargoCategory = cargoCategory; }

    public Double getCargoQuantity() { return cargoQuantity; }
    public void setCargoQuantity(Double cargoQuantity) { this.cargoQuantity = cargoQuantity; }

}
