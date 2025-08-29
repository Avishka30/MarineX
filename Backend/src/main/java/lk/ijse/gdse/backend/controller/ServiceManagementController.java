package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ServicesDTO;
import lk.ijse.gdse.backend.entity.Services;
import lk.ijse.gdse.backend.service.ServiceManagementService;
import lk.ijse.gdse.backend.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServicesController {

    private final ServiceManagementService serviceManagementService;

    public ServicesController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    // Get all services
    @GetMapping
    public ResponseEntity<APIResponse<List<ServicesDTO>>> getAllServices() {
        List<ServicesDTO> services = serviceManagementService.getAllServices();
        return ResponseEntity.ok(new APIResponse<>(true, "All services retrieved", services));
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ServicesDTO>> getServiceById(@PathVariable Long id) {
        ServicesDTO service = serviceManagementService.getServiceById(id);
        return ResponseEntity.ok(new APIResponse<>(true, "Service retrieved", service));
    }

    // Add new service
    @PostMapping
    public ResponseEntity<APIResponse<ServicesDTO>> addService(@RequestBody ServicesDTO serviceDTO) {
        ServicesDTO createdService = serviceManagementService.addService(serviceDTO);
        return new ResponseEntity<>(new APIResponse<>(true, "Service created", createdService), HttpStatus.CREATED);
    }

    // Update service
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ServicesDTO>> updateService(@PathVariable Long id, @RequestBody ServicesDTO serviceDTO) {
        ServicesDTO updatedService = serviceManagementService.updateService(id, serviceDTO);
        return ResponseEntity.ok(new APIResponse<>(true, "Service updated", updatedService));
    }

    // Delete service
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteService(@PathVariable Long id) {
        serviceManagementService.deleteService(id);
        return ResponseEntity.ok(new APIResponse<>(true, "Service deleted", null));
    }
}
