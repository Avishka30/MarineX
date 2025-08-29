package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ServicesDTO;
import lk.ijse.gdse.backend.entity.Services;
import lk.ijse.gdse.backend.repository.ServicesRepository;
import lk.ijse.gdse.backend.service.ServiceManagementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceManagementServiceImpl implements ServiceManagementService {

    private final ServicesRepository servicesRepository;

    public ServiceManagementServiceImpl(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Override
    public ServicesDTO createService(ServicesDTO dto) {
        Services service = new Services();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        Services saved = servicesRepository.save(service);
        return mapToDTO(saved);
    }

    @Override
    public ServicesDTO getServiceById(Long id) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return mapToDTO(service);
    }

    @Override
    public List<ServicesDTO> getAllServices() {
        return servicesRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicesDTO updateService(Long id, ServicesDTO dto) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        return mapToDTO(servicesRepository.save(service));
    }

    @Override
    public void deleteService(Long id) {
        servicesRepository.deleteById(id);
    }

    @Override
    public List<ServicesDTO> getServicesByName(String name) {
        return servicesRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ServicesDTO mapToDTO(Services service) {
        return new ServicesDTO(
                service.getServiceId(),
                service.getName(),
                service.getDescription(),
                service.getPrice()
        );
    }
}
