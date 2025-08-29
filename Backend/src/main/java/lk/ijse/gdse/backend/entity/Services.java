package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "service")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String name; // Fuel, Maintenance, Cleaning, etc.
    private String description;
    private Double price;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<BookingService> bookingServices;

    // Constructors
    public Services() {}

    public Services(Long serviceId, String name, String description, Double price, List<BookingService> bookingServices) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bookingServices = bookingServices;
    }

    // Getters & Setters
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<BookingService> getBookingServices() {
        return bookingServices;
    }

    public void setBookingServices(List<BookingService> bookingServices) {
        this.bookingServices = bookingServices;
    }
}
