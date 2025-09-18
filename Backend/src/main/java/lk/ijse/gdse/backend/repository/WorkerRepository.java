package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Worker;
import lk.ijse.gdse.backend.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    // Search by name (contains)
    List<Worker> findByFullNameContainingIgnoreCase(String fullName);

    // Search by specialization
    List<Worker> findBySpecialization(Specialization specialization);

    // Search by status
    List<Worker> findByStatusIgnoreCase(String status);
}
