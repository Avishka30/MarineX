package lk.ijse.gdse.backend.controller.admin;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.service.AgentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/agents")

public class AgentManagementController {

    private final AgentManagementService agentService;

    public AgentManagementController(AgentManagementService agentService) {
        this.agentService = agentService;
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<?>> approveAgent(@PathVariable Long id) {
        ApiResponse<?> response = agentService.approveAgent(id, true);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<?>> rejectAgent(@PathVariable Long id) {
        ApiResponse<?> response = agentService.approveAgent(id, false);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

}
