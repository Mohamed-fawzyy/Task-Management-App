package com.trading.task_management.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.task_management.security.entity.User;
import com.trading.task_management.security.repository.UserAuthRepo;
import com.trading.task_management.util.dto.CustomApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserAuthRepo userAuthRepo; // Directly use UserAuthRepo instead of UserDetailsService
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt, false); // throws if token is expired or invalid

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userAuthRepo.findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("User not found! or check token validity"));

                if (jwtService.isTokenValid(jwt, user, false)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendErrorResponse(response,
                    "Access token expired. Please use refresh token to get a new one.");

        } catch (JwtException e) {
            sendErrorResponse(response,
                    "Invalid token: " + e.getMessage());
        }
    }
    private void sendErrorResponse(HttpServletResponse response, String message)
            throws IOException {

        CustomApiResponse<Void> apiResponse = new CustomApiResponse<>(
                HttpServletResponse.SC_UNAUTHORIZED,
                Instant.now(),
                message,
                null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}
