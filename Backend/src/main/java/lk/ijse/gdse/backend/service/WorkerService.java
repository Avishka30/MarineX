package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.WorkerDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WorkerService {
    WorkerDTO addWorker(WorkerDTO workerDTO);
    WorkerDTO updateWorker(Long id, WorkerDTO workerDTO);
    String deleteWorker(Long id);
    WorkerDTO getWorkerById(Long id);
    List<WorkerDTO> getAllWorkers();
    List<WorkerDTO> searchWorkersByName(String name);
    List<WorkerDTO> searchWorkersBySpecialization(String specialization);
    List<WorkerDTO> searchWorkersByStatus(String status);

}
