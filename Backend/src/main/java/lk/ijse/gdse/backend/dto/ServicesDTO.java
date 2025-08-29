package lk.ijse.gdse.backend.dto;

public class ServicesDTO {
    private Long serviceId;
    private String name;
    private String description;
    private Double price;

    public ServicesDTO() {}

    public ServicesDTO(Long serviceId, String name, String description, Double price) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
