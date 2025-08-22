package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vessels")
public class Vessel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vesselId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent; // FK → User.user_id

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VesselCategory category;

    @Column(nullable = false)
    private String size; // can hold dimensions/tonnage as String

    @Column(nullable = false)
    private String companyName;

    // ===== Constructors =====
    public Vessel() {
    }

    public Vessel(User agent, String name, VesselCategory category, String size, String companyName) {
        this.agent = agent;
        this.name = name;
        this.category = category;
        this.size = size;
        this.companyName = companyName;
    }

    // ===== Getters & Setters =====
    public Long getVesselId() {
        return vesselId;
    }
    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public User getAgent() {
        return agent;
    }
    public void setAgent(User agent) {
        this.agent = agent;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public VesselCategory getCategory() {
        return category;
    }
    public void setCategory(VesselCategory category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
