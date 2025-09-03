package lk.ijse.gdse.backend.service;

import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.AuthDTO;
import lk.ijse.gdse.backend.dto.AuthResponseDTO;
import lk.ijse.gdse.backend.dto.RegisterDTO;
import lk.ijse.gdse.backend.entity.Role;
import lk.ijse.gdse.backend.entity.Status;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.util.JWTUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<?> registerAgent(RegisterDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            return ApiResponse.fail("Email already exists");
        }
        if (dto.getLicenseCode() == null || dto.getLicenseCode().isBlank()) {
            return ApiResponse.fail("License code is required");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.AGENT);
        user.setLicenseCode(dto.getLicenseCode());
        user.setStatus(Status.PENDING);

        // profile picture initially null
        user.setProfileImageUrl(null);

        userRepo.save(user);
        return ApiResponse.ok("Agent registered. Waiting for admin approval.", null);
    }

    public ApiResponse<AuthResponseDTO> login(AuthDTO dto) {
        var user = userRepo.findByEmail(dto.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            return ApiResponse.fail("Invalid credentials");
        }
        if (user.getStatus() != Status.ACTIVE) {
            return ApiResponse.fail("Account not active. Current status: " + user.getStatus());
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        AuthResponseDTO payload = new AuthResponseDTO(  user.getUserId(),   token, user.getFullName(), user.getRole().name(), "Login successful");
        return ApiResponse.ok("Login successful", payload);
    }

    public ApiResponse<?> registerAdmin(RegisterDTO dto) {
        long adminCount = userRepo.countByRole(Role.ADMIN);
        if (adminCount > 0) {
            return ApiResponse.fail("Admin registration closed. Admin already exists.");
        }
        if (userRepo.existsByEmail(dto.getEmail())) {
            return ApiResponse.fail("Email already exists");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);

        // profile picture initially null
        user.setProfileImageUrl(null);

        userRepo.save(user);
        return ApiResponse.ok("First admin registered successfully", null);
    }

    public void logout(HttpServletResponse response) {

    }
}
