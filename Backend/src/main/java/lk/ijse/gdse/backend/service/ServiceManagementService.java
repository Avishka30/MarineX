package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.ServicesDTO;
import java.util.List;

public interface ServiceManagementService {

    ServicesDTO createService(ServicesDTO serviceDTO);
    ServicesDTO getServiceById(Long id);
    List<ServicesDTO> getAllServices();
    ServicesDTO updateService(Long id, ServicesDTO serviceDTO);
    void deleteService(Long id);

    // Optional search by name
    List<ServicesDTO> getServicesByName(String name);
}
