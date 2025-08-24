package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.BerthDTO;

import java.util.List;

public interface BerthService {

    List<BerthDTO> getAllBerths();

    BerthDTO getBerthById(Long id);

    ApiResponse<BerthDTO> addBerth(BerthDTO berthDTO);

    ApiResponse<BerthDTO> updateBerth(Long id, BerthDTO berthDTO);

    ApiResponse<Void> deleteBerth(Long id);
}
