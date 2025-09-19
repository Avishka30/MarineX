package lk.ijse.gdse.backend.dto;

import java.time.LocalDate;

public class WorkerAssignmentDTO {

    private Long assignmentId;
    private Long bookingServiceId; // only the ID
    private Long workerId;         // only the ID
    private LocalDate assignedDate;

    // Default constructor
    public WorkerAssignmentDTO() {
    }

    // Constructor without ID (for create requests)
    public WorkerAssignmentDTO(Long bookingServiceId, Long workerId, LocalDate assignedDate) {
        this.bookingServiceId = bookingServiceId;
        this.workerId = workerId;
        this.assignedDate = assignedDate;
    }

    // Full constructor
    public WorkerAssignmentDTO(Long assignmentId, Long bookingServiceId, Long workerId, LocalDate assignedDate) {
        this.assignmentId = assignmentId;
        this.bookingServiceId = bookingServiceId;
        this.workerId = workerId;
        this.assignedDate = assignedDate;
    }

    // Getters and Setters
    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getBookingServiceId() {
        return bookingServiceId;
    }

    public void setBookingServiceId(Long bookingServiceId) {
        this.bookingServiceId = bookingServiceId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
}
