package lk.ijse.gdse.backend.dto;

import lk.ijse.gdse.backend.entity.Specialization;

public class WorkerDTO {

    private Long workerId;
    private String fullName;
    private String email;
    private String phone;
    private Specialization specialization;
    private String status; // Active / Inactive

    // --- Constructors ---
    public WorkerDTO() {
    }

    public WorkerDTO(Long workerId, String fullName, String email, String phone, Specialization specialization, String status) {
        this.workerId = workerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.status = status;
    }

    // --- Getters and Setters ---
    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}