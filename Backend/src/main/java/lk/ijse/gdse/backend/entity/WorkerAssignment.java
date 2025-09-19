package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class WorkerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "booking_service_id", nullable = false)
    private BookingService bookingService;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    private LocalDate assignedDate = LocalDate.now();

    // Default constructor
    public WorkerAssignment() {
    }

    // Constructor with all fields except ID
    public WorkerAssignment(BookingService bookingService, Worker worker, LocalDate assignedDate) {
        this.bookingService = bookingService;
        this.worker = worker;
        this.assignedDate = assignedDate;
    }

    // Full constructor (with ID)
    public WorkerAssignment(Long assignmentId, BookingService bookingService, Worker worker, LocalDate assignedDate) {
        this.assignmentId = assignmentId;
        this.bookingService = bookingService;
        this.worker = worker;
        this.assignedDate = assignedDate;
    }

    // Getters and Setters
    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
}
