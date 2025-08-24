package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.entity.Vessel;
import lk.ijse.gdse.backend.entity.VesselCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Long> {

    List<Vessel> findByAgent(User agent);

    List<Vessel> findByCategory(VesselCategory category);

    List<Vessel> findByCompanyNameContainingIgnoreCase(String companyName);

    List<Vessel> findByName(String name);
}
