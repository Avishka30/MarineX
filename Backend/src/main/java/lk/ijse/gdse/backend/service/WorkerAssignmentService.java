package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.WorkerAssignmentDTO;

import java.util.List;

public interface WorkerAssignmentService {

    WorkerAssignmentDTO assignWorker(WorkerAssignmentDTO dto);

    List<WorkerAssignmentDTO> getAssignmentsByBookingService(Long bookingServiceId);

    List<WorkerAssignmentDTO> getAllAssignments();
}
