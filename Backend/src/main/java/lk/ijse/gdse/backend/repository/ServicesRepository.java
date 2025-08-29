package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

    // Optional method to search services by name (case-insensitive)
    List<Services> findByNameContainingIgnoreCase(String name);
}
