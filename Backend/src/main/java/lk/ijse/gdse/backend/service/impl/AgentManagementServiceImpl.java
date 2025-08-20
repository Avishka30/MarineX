package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.Role;
import lk.ijse.gdse.backend.entity.Status;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.repository.AgentRepository;
import lk.ijse.gdse.backend.service.AgentManagementService;
import lk.ijse.gdse.backend.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentManagementServiceImpl implements AgentManagementService {

    private final AgentRepository agentRepo;
    private final EmailService emailService;

    public AgentManagementServiceImpl(AgentRepository agentRepo, EmailService emailService) {
        this.agentRepo = agentRepo;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public ApiResponse<?> approveAgent(Long agentId, boolean approve) {
        User agent = agentRepo.findByUserIdAndRole(agentId, Role.AGENT)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (approve) {
            agent.setStatus(Status.ACTIVE);
            agentRepo.save(agent);

            try {
                emailService.sendEmail(
                        agent.getEmail(),
                        "Agent Approval",
                        "Congratulations " + agent.getFullName() +
                                ", your account has been approved by the admin. You can now log in."
                );
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
            }

            return new ApiResponse<>(true, "Agent approved successfully", null);

        } else {
            agent.setStatus(Status.REJECTED);
            agentRepo.save(agent);

            try {
                emailService.sendEmail(
                        agent.getEmail(),
                        "Agent Registration Rejected",
                        "Dear " + agent.getFullName() +
                                ", unfortunately your agent request was rejected by the admin."
                );
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
            }

            return new ApiResponse<>(true, "Agent rejected successfully", null);
        }
    }
}
