package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.WorkerDTO;
import lk.ijse.gdse.backend.service.WorkerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping
    public ResponseEntity<WorkerDTO> addWorker(@RequestBody WorkerDTO workerDTO) {
        return ResponseEntity.ok(workerService.addWorker(workerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkerDTO> updateWorker(@PathVariable Long id, @RequestBody WorkerDTO workerDTO) {
        return ResponseEntity.ok(workerService.updateWorker(id, workerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.deleteWorker(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerDTO> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<WorkerDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(workerService.searchWorkersByName(name));
    }

    @GetMapping("/search/specialization")
    public ResponseEntity<List<WorkerDTO>> searchBySpecialization(@RequestParam String specialization) {
        return ResponseEntity.ok(workerService.searchWorkersBySpecialization(specialization));
    }

    @GetMapping("/search/status")
    public ResponseEntity<List<WorkerDTO>> searchByStatus(@RequestParam String status) {
        return ResponseEntity.ok(workerService.searchWorkersByStatus(status));
    }


}
