package lk.ijse.gdse.backend.dto;

public class AuthResponseDTO {
    private String token;
    private String fullName;
    private String role;
    private String message;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String fullName, String role, String message) {
        this.token = token;
        this.fullName = fullName;
        this.role = role;
        this.message = message;
    }

    // getters & setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
