package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.entity.Berth;

import java.util.List;

public interface BerthService {

    // Get all berths
    List<Berth> getAllBerths();

    // Get berth by ID
    Berth getBerthById(Long id);

    // Add a new berth
    ApiResponse<Berth> addBerth(Berth berth);

    // Update existing berth
    ApiResponse<Berth> updateBerth(Long id, Berth berth);

    // Delete a berth
    ApiResponse<Void> deleteBerth(Long id);
}
