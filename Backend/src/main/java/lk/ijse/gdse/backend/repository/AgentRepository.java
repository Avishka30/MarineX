package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Role;
import lk.ijse.gdse.backend.entity.Status;
import lk.ijse.gdse.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<User, Long> {

    // Find all agents
    List<User> findByRole(Role role);

    // Find active agents
    List<User> findByRoleAndStatus(Role role, Status status);

    // Find agent by userId and role (fixed)
    Optional<User> findByUserIdAndRole(Long userId, Role role);
}
