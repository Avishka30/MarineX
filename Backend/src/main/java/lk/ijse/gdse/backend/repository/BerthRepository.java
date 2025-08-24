package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Berth;
import lk.ijse.gdse.backend.entity.BerthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BerthRepository extends JpaRepository<Berth, Long> {

    //custom q
    List<Berth> findByStatus(BerthStatus status);

    Berth findByBerthNumber(String berthNumber);
}
