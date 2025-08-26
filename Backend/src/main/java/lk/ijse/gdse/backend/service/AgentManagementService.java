package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.User;

import java.util.List;

public interface AgentManagementService {
    /**
     * Approve or reject an agent.
     *
     * @param usrId the ID of the agent
     * @param approve true to approve, false to reject
     * @return ApiResponse with success or error message
     */
    ApiResponse<?> approveAgent(Long usrId, boolean approve);

    ApiResponse<?> getAllPendingAgents();
}
