package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.Vessel;
import lk.ijse.gdse.backend.service.VesselService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vessels")
public class VesselManagementController {

    private final VesselService vesselService;

    public VesselManagementController(VesselService vesselService) {
        this.vesselService = vesselService;
    }

    // ===== Get all vessels =====
    @GetMapping
    public ResponseEntity<List<Vessel>> getAllVessels() {
        List<Vessel> vessels = vesselService.getAllVessels();
        return ResponseEntity.ok(vessels);
    }

    // ===== Get vessel by ID =====
    @GetMapping("/{id}")
    public ResponseEntity<Vessel> getVesselById(@PathVariable Long id) {
        Vessel vessel = vesselService.getVesselById(id);
        return ResponseEntity.ok(vessel);
    }

    // ===== Add a new vessel =====
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Vessel>> addVessel(@RequestBody Vessel vessel) {
        ApiResponse<Vessel> response = vesselService.addVessel(vessel);
        return ResponseEntity.status(response.isSuccess() ? 201 : 400).body(response);
    }

    // ===== Update existing vessel =====
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Vessel>> updateVessel(@PathVariable Long id, @RequestBody Vessel vessel) {
        ApiResponse<Vessel> response = vesselService.updateVessel(id, vessel);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    // ===== Delete vessel =====
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVessel(@PathVariable Long id) {
        ApiResponse<Void> response = vesselService.deleteVessel(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }
}
