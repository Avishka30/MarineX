package lk.ijse.gdse.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/test")
    public String testAdminOnly() {
        return "Hello Admin!";
    }
}
