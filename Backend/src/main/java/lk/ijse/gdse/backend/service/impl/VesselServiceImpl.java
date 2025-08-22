package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.Vessel;
import lk.ijse.gdse.backend.repository.VesselRepository;
import lk.ijse.gdse.backend.service.VesselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VesselServiceImpl implements VesselService {

    private final VesselRepository vesselRepository;

    public VesselServiceImpl(VesselRepository vesselRepository) {
        this.vesselRepository = vesselRepository;
    }

    @Override
    public List<Vessel> getAllVessels() {
        return vesselRepository.findAll();
    }

    @Override
    public Vessel getVesselById(Long id) {
        return vesselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vessel not found with id: " + id));
    }

    @Override
    @Transactional
    public ApiResponse<Vessel> addVessel(Vessel vessel) {
        Vessel saved = vesselRepository.save(vessel);
        return new ApiResponse<>(true, "Vessel added successfully", saved);
    }

    @Override
    @Transactional
    public ApiResponse<Vessel> updateVessel(Long id, Vessel vessel) {
        Vessel existing = vesselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vessel not found with id: " + id));

        // Update fields
        existing.setName(vessel.getName());
        existing.setCategory(vessel.getCategory());
        existing.setSize(vessel.getSize());
        existing.setCompanyName(vessel.getCompanyName());
        existing.setAgent(vessel.getAgent());

        Vessel updated = vesselRepository.save(existing);
        return new ApiResponse<>(true, "Vessel updated successfully", updated);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteVessel(Long id) {
        Vessel existing = vesselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vessel not found with id: " + id));

        vesselRepository.delete(existing);
        return new ApiResponse<>(true, "Vessel deleted successfully", null);
    }
}
