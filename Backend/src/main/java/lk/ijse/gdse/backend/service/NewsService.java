package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.NewsDTO;

import java.util.List;

public interface NewsService {

    List<NewsDTO> getAllNews();

    NewsDTO getNewsById(Long id);

    ApiResponse<NewsDTO> addNews(NewsDTO newsDTO);

    ApiResponse<NewsDTO> updateNews(Long id, NewsDTO newsDTO);

    ApiResponse<Void> deleteNews(Long id);
}
