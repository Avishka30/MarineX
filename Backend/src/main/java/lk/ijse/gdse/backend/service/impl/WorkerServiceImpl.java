package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.WorkerDTO;
import lk.ijse.gdse.backend.entity.Worker;
import lk.ijse.gdse.backend.entity.Specialization;
import lk.ijse.gdse.backend.repository.WorkerRepository;
import lk.ijse.gdse.backend.service.WorkerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerServiceImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    public WorkerDTO addWorker(WorkerDTO workerDTO) {
        Worker worker = toEntity(workerDTO);
        worker = workerRepository.save(worker);
        return toDTO(worker);
    }

    @Override
    public WorkerDTO updateWorker(Long id, WorkerDTO workerDTO) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        worker.setFullName(workerDTO.getFullName());
        worker.setEmail(workerDTO.getEmail());
        worker.setPhone(workerDTO.getPhone());
        worker.setSpecialization(workerDTO.getSpecialization());
        worker.setStatus(workerDTO.getStatus());

        workerRepository.save(worker);
        return toDTO(worker);
    }

    @Override
    public String deleteWorker(Long id) {
        if (!workerRepository.existsById(id)) {
            throw new RuntimeException("Worker not found");
        }
        workerRepository.deleteById(id);
        return "Worker deleted successfully";
    }

    @Override
    public WorkerDTO getWorkerById(Long id) {
        return workerRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    @Override
    public List<WorkerDTO> getAllWorkers() {
        return workerRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkerDTO> searchWorkersByName(String name) {
        return workerRepository.findByFullNameContainingIgnoreCase(name)
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkerDTO> searchWorkersBySpecialization(String specialization) {
        return workerRepository.findBySpecialization(Specialization.valueOf(specialization.toUpperCase()))
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkerDTO> searchWorkersByStatus(String status) {
        return workerRepository.findByStatusIgnoreCase(status)
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Mapper Methods ---
    private WorkerDTO toDTO(Worker worker) {
        return new WorkerDTO(
                worker.getWorkerId(),
                worker.getFullName(),
                worker.getEmail(),
                worker.getPhone(),
                worker.getSpecialization(),
                worker.getStatus()
        );
    }

    private Worker toEntity(WorkerDTO dto) {
        return new Worker(
                dto.getWorkerId(),
                dto.getFullName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getSpecialization(),
                dto.getStatus()
        );
    }


}
