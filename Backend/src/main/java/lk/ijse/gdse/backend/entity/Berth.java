package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "berths")
public class Berth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long berthId;

    @Column(nullable = false, unique = true)
    private String berthNumber;

    @Column(nullable = false)
    private String capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BerthStatus status;

    public Berth() {
    }

    public Berth(Long berthId, String berthNumber, String capacity, BerthStatus status) {
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

    public BerthStatus getStatus() {
        return status;
    }

    public void setStatus(BerthStatus status) {
        this.status = status;
    }
}
