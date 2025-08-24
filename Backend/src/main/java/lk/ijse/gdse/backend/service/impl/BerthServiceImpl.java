package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.Berth;
import lk.ijse.gdse.backend.repository.BerthRepository;
import lk.ijse.gdse.backend.service.BerthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BerthServiceImpl implements BerthService {

    private final BerthRepository berthRepository;

    public BerthServiceImpl(BerthRepository berthRepository) {
        this.berthRepository = berthRepository;
    }

    @Override
    public List<Berth> getAllBerths() {
        return berthRepository.findAll();
    }

    @Override
    public Berth getBerthById(Long id) {
        return berthRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Berth not found with ID: " + id));
    }

    @Override
    public ApiResponse<Berth> addBerth(Berth berth) {
        Berth savedBerth = berthRepository.save(berth);
        return new ApiResponse<>(true, "Berth added successfully", savedBerth);
    }

    @Override
    public ApiResponse<Berth> updateBerth(Long id, Berth berth) {
        return berthRepository.findById(id)
                .map(existingBerth -> {
                    existingBerth.setBerthNumber(berth.getBerthNumber());
                    existingBerth.setCapacity(berth.getCapacity());
                    existingBerth.setStatus(berth.getStatus());
                    Berth updated = berthRepository.save(existingBerth);
                    return new ApiResponse<>(true, "Berth updated successfully", updated);
                })
                .orElseGet(() -> new ApiResponse<>(false, "Berth not found with ID: " + id, null));
    }

    @Override
    public ApiResponse<Void> deleteBerth(Long id) {
        return berthRepository.findById(id)
                .map(existingBerth -> {
                    berthRepository.deleteById(id);
                    return new ApiResponse<Void>(true, "Berth deleted successfully", null);
                })
                .orElseGet(() -> new ApiResponse<>(false, "Berth not found with ID: " + id, null));
    }
}
