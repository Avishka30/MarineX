package lk.ijse.gdse.backend.dto;

public class BerthDTO {

    private Long berthId;
    private String berthNumber;
    private String capacity;
    private String status;

    public BerthDTO() {
    }

    public BerthDTO(Long berthId, String berthNumber, String capacity, String status) {
        this.berthId = berthId;
        this.berthNumber = berthNumber;
        this.capacity = capacity;
        this.status = status;
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
}
