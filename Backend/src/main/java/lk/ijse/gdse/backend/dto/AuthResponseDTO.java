package lk.ijse.gdse.backend.dto;

public class AuthResponseDTO {
    private String token;
    private String fullName;
    private String message;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String fullName, String message) {
        this.token = token;
        this.fullName = fullName;
        this.message = message;
    }

    // Getters and setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
