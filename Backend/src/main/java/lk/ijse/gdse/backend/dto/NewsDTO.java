package lk.ijse.gdse.backend.dto;

import java.time.LocalDateTime;

public class NewsDTO {

    private Long newsId;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private String status;

    public NewsDTO() {
    }

    public NewsDTO(Long newsId, String title, String content, LocalDateTime publishedDate, String status) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.publishedDate = publishedDate;
        this.status = status;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
