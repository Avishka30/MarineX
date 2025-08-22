package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.entity.Vessel;
import lk.ijse.gdse.backend.entity.VesselCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Long> {

    // Find all vessels by a specific agent
    List<Vessel> findByAgent(User agent);

    // Find vessels by category
    List<Vessel> findByCategory(VesselCategory category);

    // Optional: find vessels by company name (contains search)
    List<Vessel> findByCompanyNameContainingIgnoreCase(String companyName);

    // Optional: find by name (exact match)
    List<Vessel> findByName(String name);
}
