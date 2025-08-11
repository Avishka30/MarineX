package lk.ijse.gdse.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.AuthDTO;
import lk.ijse.gdse.backend.dto.AuthResponseDTO;
import lk.ijse.gdse.backend.dto.RegisterDTO;
import lk.ijse.gdse.backend.entity.Role;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/auth")
public class UserController {
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-agent")
    public ResponseEntity<ApiResponse<?>> registerAgent(@RequestBody RegisterDTO dto) {
        var resp = authService.registerAgent(dto);
        return ResponseEntity.status(resp.isSuccess() ? 200 : 400).body(resp);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<?>> registerAdmin(@RequestBody RegisterDTO dto) {
        var resp = authService.registerAdmin(dto);
        return ResponseEntity.status(resp.isSuccess() ? 200 : 400).body(resp);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody AuthDTO dto, HttpServletResponse response) {
        var resp = authService.login(dto, response);
        return ResponseEntity.status(resp.isSuccess() ? 200 : 401).body(resp);
    }

   /* @GetMapping("/admin-exists")
    public ResponseEntity<Boolean> adminExists() {
        boolean exists = userRepo.countByRole(Role.ADMIN) > 0;
        return ResponseEntity.ok(exists);
    }*/

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(ApiResponse.ok("Logged out", null));
    }
}
