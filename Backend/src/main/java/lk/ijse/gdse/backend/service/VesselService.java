package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.entity.Vessel;
import lk.ijse.gdse.backend.dto.ApiResponse;

import java.util.List;

public interface VesselService {

    // Get all vessels
    List<Vessel> getAllVessels();

    // Get vessel by ID
    Vessel getVesselById(Long id);

    // Add a new vessel
    ApiResponse<Vessel> addVessel(Vessel vessel);

    // Update existing vessel
    ApiResponse<Vessel> updateVessel(Long id, Vessel vessel);

    // Delete a vessel
    ApiResponse<Void> deleteVessel(Long id);
}
