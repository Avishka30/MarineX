package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.VesselDTO;
import lk.ijse.gdse.backend.dto.ApiResponse;

import java.util.List;

public interface VesselService {

    List<VesselDTO> getAllVessels();

    VesselDTO getVesselById(Long id);

    ApiResponse<VesselDTO> addVessel(VesselDTO vesselDTO);

    ApiResponse<VesselDTO> updateVessel(Long id, VesselDTO vesselDTO);
    
    ApiResponse<Void> deleteVessel(Long id);
}
