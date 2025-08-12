package lk.ijse.gdse.backend.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lk.ijse.gdse.backend.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JWTAuthFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        String token = null;

        // 1) Authorization header (Bearer ...)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Token from Authorization header: " + token);
        }

        // 2) Cookie fallback
        if (token == null && request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(c -> "token".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
            System.out.println("Token from cookie: " + token);
        }

        if (token != null && jwtUtil.validateToken(token) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            String email = jwtUtil.extractSubject(token);
            String role = jwtUtil.extractRole(token);
            System.out.printf("Valid token for email=%s role=%s%n", email, role);

            var userDetails = userDetailsService.loadUserByUsername(email);

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // helpful debug
            if (token == null) {
                System.out.println("No token found in request (auth header or cookies).");
            } else {
                System.out.println("Token present but invalid or context already authenticated.");
            }
        }

        chain.doFilter(request, response);
    }
}
