package lk.ijse.gdse.backend.dto;

public class VesselDTO {

    private Long vesselId;
    private Long agentId;
    private String name;
    private String category;
    private String size;
    private String companyName;

    public VesselDTO() {
    }

    public VesselDTO(Long vesselId, Long agentId, String name, String category, String size, String companyName) {
        this.vesselId = vesselId;
        this.agentId = agentId;
        this.name = name;
        this.category = category;
        this.size = size;
        this.companyName = companyName;
    }

    public Long getVesselId() {
        return vesselId;
    }

    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
