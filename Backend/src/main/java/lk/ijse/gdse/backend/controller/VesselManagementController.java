package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.VesselDTO;
import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.service.VesselService;
import lk.ijse.gdse.backend.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vessels")
public class VesselManagementController {

    private final VesselService vesselService;

    public VesselManagementController(VesselService vesselService, JWTUtil jwtUtil) {
        this.vesselService = vesselService;
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<VesselDTO>> getAllVessels() {
        return ResponseEntity.ok(vesselService.getAllVessels());
    }

    // Get vessel by ID
    @GetMapping("/{id}")
    public ResponseEntity<VesselDTO> getVesselById(@PathVariable Long id) {
        return ResponseEntity.ok(vesselService.getVesselById(id));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<VesselDTO>> getVesselsByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(vesselService.getVesselsByAgent(agentId));
    }

    // Add vessel
    @PostMapping
    public ResponseEntity<ApiResponse<VesselDTO>> addVessel(@RequestBody VesselDTO vesselDTO) {
        ApiResponse<VesselDTO> response = vesselService.addVessel(vesselDTO);
        return ResponseEntity.status(response.isSuccess() ? 201 : 400).body(response);
    }

    // Update vessel
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VesselDTO>> updateVessel(@PathVariable Long id, @RequestBody VesselDTO vesselDTO) {
        ApiResponse<VesselDTO> response = vesselService.updateVessel(id, vesselDTO);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    // Delete vessel
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVessel(@PathVariable Long id) {
        ApiResponse<Void> response = vesselService.deleteVessel(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }
}
