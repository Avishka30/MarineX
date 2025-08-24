package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.BerthDTO;
import lk.ijse.gdse.backend.service.BerthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/berths")
public class BerthController {

    private final BerthService berthService;

    public BerthController(BerthService berthService) {
        this.berthService = berthService;
    }

    @GetMapping
    public ResponseEntity<List<BerthDTO>> getAllBerths() {
        return ResponseEntity.ok(berthService.getAllBerths());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BerthDTO> getBerthById(@PathVariable Long id) {
        return ResponseEntity.ok(berthService.getBerthById(id));
    }

    // Add berth
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BerthDTO>> addBerth(@RequestBody BerthDTO berthDTO) {
        return ResponseEntity.ok(berthService.addBerth(berthDTO));
    }

    // Update berth
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BerthDTO>> updateBerth(@PathVariable Long id, @RequestBody BerthDTO berthDTO) {
        return ResponseEntity.ok(berthService.updateBerth(id, berthDTO));
    }

    // Delete berth
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBerth(@PathVariable Long id) {
        return ResponseEntity.ok(berthService.deleteBerth(id));
    }
}
