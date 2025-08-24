package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.NewsDTO;
import lk.ijse.gdse.backend.entity.News;
import lk.ijse.gdse.backend.entity.NewsStatus;
import lk.ijse.gdse.backend.repository.NewsRepository;
import lk.ijse.gdse.backend.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    private NewsDTO toDTO(News news) {
        return new NewsDTO(
                news.getNewsId(),
                news.getTitle(),
                news.getContent(),
                news.getPublishedDate(),
                news.getStatus().name()
        );
    }

    private News toEntity(NewsDTO dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setPublishedDate(dto.getPublishedDate());
        news.setStatus(NewsStatus.valueOf(dto.getStatus()));
        return news;
    }

    @Override
    public List<NewsDTO> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NewsDTO getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public ApiResponse<NewsDTO> addNews(NewsDTO newsDTO) {
        News news = toEntity(newsDTO);
        News saved = newsRepository.save(news);
        return new ApiResponse<>(true, "News added successfully", toDTO(saved));
    }

    @Override
    public ApiResponse<NewsDTO> updateNews(Long id, NewsDTO newsDTO) {
        return newsRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(newsDTO.getTitle());
                    existing.setContent(newsDTO.getContent());
                    existing.setPublishedDate(newsDTO.getPublishedDate());
                    existing.setStatus(NewsStatus.valueOf(newsDTO.getStatus()));
                    News updated = newsRepository.save(existing);
                    return new ApiResponse<>(true, "News updated successfully", toDTO(updated));
                })
                .orElseGet(() -> new ApiResponse<>(false, "News not found", null));
    }

    @Override
    public ApiResponse<Void> deleteNews(Long id) {
        return newsRepository.findById(id)
                .map(existing -> {
                    newsRepository.deleteById(id);
                    return new ApiResponse<Void>(true, "News deleted successfully", null);
                })
                .orElseGet(() -> new ApiResponse<>(false, "News not found", null));
    }
}
