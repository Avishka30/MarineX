package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.WorkerAssignmentDTO;
import lk.ijse.gdse.backend.service.WorkerAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worker-assignments")
public class WorkerAssignmentController {

    private final WorkerAssignmentService assignmentService;

    public WorkerAssignmentController(WorkerAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<WorkerAssignmentDTO> assignWorker(@RequestBody WorkerAssignmentDTO dto) {
        WorkerAssignmentDTO saved = assignmentService.assignWorker(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/booking/{bookingServiceId}")
    public ResponseEntity<List<WorkerAssignmentDTO>> getAssignmentsByBooking(@PathVariable Long bookingServiceId) {
        List<WorkerAssignmentDTO> list = assignmentService.getAssignmentsByBookingService(bookingServiceId);
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<List<WorkerAssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
}
