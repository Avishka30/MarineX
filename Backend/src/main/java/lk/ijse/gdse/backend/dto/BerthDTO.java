package lk.ijse.gdse.backend.dto;

import java.math.BigDecimal;

public class BerthDTO {

    private Long berthId;
    private String berthNumber;
    private String capacity;
    private String status;
    private BigDecimal price; // 💰 new field

    public BerthDTO() {
    }

    public BerthDTO(Long berthId, String berthNumber, String capacity, String status, BigDecimal price) {
        this.berthId = berthId;
        this.berthNumber = berthNumber;
        this.capacity = capacity;
        this.status = status;
        this.price = price;
    }

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public String getBerthNumber() {
        return berthNumber;
    }

    public void setBerthNumber(String berthNumber) {
        this.berthNumber = berthNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
