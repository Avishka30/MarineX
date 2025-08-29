package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.ServicesDTO;
import lk.ijse.gdse.backend.service.ServiceManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceManagementController {

    private final ServiceManagementService serviceManagementService;

    public ServiceManagementController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServicesDTO>>> getAllServices() {
        List<ServicesDTO> services = serviceManagementService.getAllServices();
        return ResponseEntity.ok(new ApiResponse<>(true, "All services fetched", services));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServicesDTO>> getServiceById(@PathVariable Long id) {
        ServicesDTO service = serviceManagementService.getServiceById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service fetched", service));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServicesDTO>> createService(@RequestBody ServicesDTO dto) {
        ServicesDTO created = serviceManagementService.createService(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServicesDTO>> updateService(@PathVariable Long id, @RequestBody ServicesDTO dto) {
        ServicesDTO updated = serviceManagementService.updateService(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceManagementService.deleteService(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ServicesDTO>>> searchByName(@RequestParam String name) {
        List<ServicesDTO> services = serviceManagementService.getServicesByName(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results fetched", services));
    }
}
