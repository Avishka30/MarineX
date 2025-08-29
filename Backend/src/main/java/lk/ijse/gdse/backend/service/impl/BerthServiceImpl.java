package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.BerthDTO;
import lk.ijse.gdse.backend.entity.Berth;
import lk.ijse.gdse.backend.entity.BerthStatus;
import lk.ijse.gdse.backend.repository.BerthRepository;
import lk.ijse.gdse.backend.service.BerthService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BerthServiceImpl implements BerthService {

    private final BerthRepository berthRepository;

    public BerthServiceImpl(BerthRepository berthRepository) {
        this.berthRepository = berthRepository;
    }

    private BerthDTO toDTO(Berth berth) {
        return new BerthDTO(
                berth.getBerthId(),
                berth.getBerthNumber(),
                berth.getCapacity(),
                berth.getStatus().name(),
                berth.getPrice() // map price
        );
    }

    private Berth toEntity(BerthDTO dto) {
        BerthStatus status;
        try {
            status = BerthStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid berth status: " + dto.getStatus());
        }

        Berth berth = new Berth();
        berth.setBerthId(dto.getBerthId());
        berth.setBerthNumber(dto.getBerthNumber());
        berth.setCapacity(dto.getCapacity());
        berth.setStatus(status);
        berth.setPrice(dto.getPrice()); // set price
        return berth;
    }

    @Override
    public List<BerthDTO> getAllBerths() {
        return berthRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BerthDTO getBerthById(Long id) {
        Berth berth = berthRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Berth not found with ID: " + id));
        return toDTO(berth);
    }

    @Override
    public ApiResponse<BerthDTO> addBerth(BerthDTO berthDTO) {
        Berth berth = toEntity(berthDTO);
        Berth saved = berthRepository.save(berth);
        return new ApiResponse<>(true, "Berth added successfully", toDTO(saved));
    }

    @Override
    public ApiResponse<BerthDTO> updateBerth(Long id, BerthDTO berthDTO) {
        return berthRepository.findById(id)
                .map(existing -> {
                    existing.setBerthNumber(berthDTO.getBerthNumber());
                    existing.setCapacity(berthDTO.getCapacity());
                    existing.setStatus(BerthStatus.valueOf(berthDTO.getStatus().toUpperCase()));
                    existing.setPrice(berthDTO.getPrice()); // update price
                    Berth updated = berthRepository.save(existing);
                    return new ApiResponse<>(true, "Berth updated successfully", toDTO(updated));
                })
                .orElseGet(() -> new ApiResponse<>(false, "Berth not found with ID: " + id, null));
    }

    @Override
    public ApiResponse<Void> deleteBerth(Long id) {
        return berthRepository.findById(id)
                .map(existing -> {
                    berthRepository.deleteById(id);
                    return new ApiResponse<Void>(true, "Berth deleted successfully", null);
                })
                .orElseGet(() -> new ApiResponse<>(false, "Berth not found with ID: " + id, null));
    }
}
