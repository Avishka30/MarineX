package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.Berth;
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

    // ✅ Get all berths
    @GetMapping
    public ResponseEntity<List<Berth>> getAllBerths() {
        return ResponseEntity.ok(berthService.getAllBerths());
    }

    // ✅ Get berth by ID
    @GetMapping("/{id}")
    public ResponseEntity<Berth> getBerthById(@PathVariable Long id) {
        return ResponseEntity.ok(berthService.getBerthById(id));
    }

    // ✅ Add new berth
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Berth>> addBerth(@RequestBody Berth berth) {
        return ResponseEntity.ok(berthService.addBerth(berth));
    }

    // ✅ Update berth
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Berth>> updateBerth(@PathVariable Long id, @RequestBody Berth berth) {
        return ResponseEntity.ok(berthService.updateBerth(id, berth));
    }

    // ✅ Delete berth
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBerth(@PathVariable Long id) {
        return ResponseEntity.ok(berthService.deleteBerth(id));
    }
}
