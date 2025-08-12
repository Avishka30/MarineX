package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentDashboardController {

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> testAgentAccess() {
        return ResponseEntity.ok(ApiResponse.ok("Agent access successful", "Agent dashboard test data"));
    }

}
