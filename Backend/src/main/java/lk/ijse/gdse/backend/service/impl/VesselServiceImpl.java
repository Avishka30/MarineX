package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.VesselDTO;
import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.entity.Vessel;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.repository.VesselRepository;
import lk.ijse.gdse.backend.service.VesselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VesselServiceImpl implements VesselService {

    private final VesselRepository vesselRepository;
    private final UserRepository userRepository;

    public VesselServiceImpl(VesselRepository vesselRepository, UserRepository userRepository) {
        this.vesselRepository = vesselRepository;
        this.userRepository = userRepository;
    }

    private VesselDTO toDTO(Vessel vessel) {
        return new VesselDTO(
                vessel.getVesselId(),
                vessel.getAgent().getUserId(),
                vessel.getName(),
                vessel.getCategory().name(),
                vessel.getSize(),
                vessel.getCompanyName()
        );
    }

    private Vessel toEntity(VesselDTO dto) {
        User agent = userRepository.findById(dto.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + dto.getAgentId()));
        return new Vessel(agent, dto.getName(),
                Enum.valueOf(lk.ijse.gdse.backend.entity.VesselCategory.class, dto.getCategory()),
                dto.getSize(), dto.getCompanyName());
    }

    @Override
    public List<VesselDTO> getAllVessels() {
        return vesselRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VesselDTO getVesselById(Long id) {
        Vessel vessel = vesselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vessel not found with id: " + id));
        return toDTO(vessel);
    }

    @Override
    @Transactional
    public ApiResponse<VesselDTO> addVessel(VesselDTO vesselDTO) {
        Vessel saved = vesselRepository.save(toEntity(vesselDTO));
        return new ApiResponse<>(true, "Vessel added successfully", toDTO(saved));
    }

    @Override
    @Transactional
    public ApiResponse<VesselDTO> updateVessel(Long id, VesselDTO vesselDTO) {
        Vessel existing = vesselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vessel not found with id: " + id));

        User agent = userRepository.findById(vesselDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + vesselDTO.getAgentId()));

        existing.setAgent(agent);
        existing.setName(vesselDTO.getName());
        existing.setCategory(Enum.valueOf(lk.ijse.gdse.backend.entity.VesselCategory.class, vesselDTO.getCategory()));
        existing.setSize(vesselDTO.getSize());
        existing.setCompanyName(vesselDTO.getCompanyName());

        Vessel updated = vesselRepository.save(existing);
        return new ApiResponse<>(true, "Vessel updated successfully", toDTO(updated));
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
