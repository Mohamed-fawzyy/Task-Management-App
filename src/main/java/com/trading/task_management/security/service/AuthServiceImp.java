package com.trading.task_management.security.service;

import com.trading.task_management.exceptions.*;
import com.trading.task_management.security.config.JwtService;
import com.trading.task_management.security.dto.*;
import com.trading.task_management.security.entity.*;
import com.trading.task_management.security.repository.RefreshTokenRepository;
import com.trading.task_management.security.repository.UserAuthRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handles authentication, registration, verification, and token lifecycle logic.
 */

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserAuthRepo repo;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user by encoding the password, saving the user as unverified,
     * and generating a 6-character email verification code that is sent to the user's email.
     * Any existing verification token for the user is deleted.
     *
     * @param req registration request containing firstName, lastName, email, and password
     * @return response entity with a message instructing user to check their email
     * @throws DuplicateResourceException if a user with the provided email already exists
     */
    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponse> register(RegisterRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new DuplicateResourceException("Email already in use.");
        }

        User user = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .userRole(UserRole.USER)
                .build();

        repo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthenticationResponse(HttpStatus.CREATED.value(), "User registered. Please Login for authentication."));
    }

    /**
     * Validates the email and password of the user. Ensures the user has verified
     * their email before issuing a JWT access and refresh token. Existing refresh
     * tokens are deleted before assigning the new one.
     *
     * @param req login request containing email and password
     * @return JWT access and refresh tokens
     * @throws ResourceNotFoundException      if user does not exist
     * @throws ResourceForbiddenException     if user email is not verified
     * @throws ResourceNotAuthorizedException for invalid credentials
     * @throws DuplicateResourceException     if refresh token generation fails
     */
    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest req) {
        var user = repo.findByEmail(req.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password()));

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            saveNewRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
        } catch (BadCredentialsException ex) {
            throw new ResourceNotAuthorizedException("Invalid email or password.");
        } catch (Exception ex) {
            throw new DuplicateResourceException("Token generation error: " + ex.getMessage());
        }
    }

    /**
     * Creates a new refresh token, deletes the previous one if it exists, and
     * assigns it to the user.
     *
     * @param user       the user to whom the token belongs
     * @param tokenValue the refresh token string
     */
    @Transactional
    protected void saveNewRefreshToken(User user, String tokenValue) {
        // Remove the existing token if it exists
        if (user.getRefreshToken() != null) {
            user.setRefreshToken(null);  // orphanRemoval = true will delete the old one and unlink
            repo.saveAndFlush(user);  // 🧨 Force DB to delete old token before inserting new one
        }

        RefreshToken newToken = RefreshToken.builder()
                .token(tokenValue)
                .expiryDate(LocalDateTime.now().plusDays(3))
                .user(user)
                .build();

        user.setRefreshToken(newToken); // link the new token to the user
        repo.save(user); // this save is for inserting
    }

    /**
     * Refreshes the access token using a valid and non-expired refresh token.
     * Deletes the old refresh token and issues a new one with extended expiry.
     *
     * @param oldRefreshToken the previous refresh token string
     * @return new access and refresh tokens
     * @throws ResourceBadRequestException if token is invalid
     * @throws ResourceForbiddenException if token has expired
     */
    @Override
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(String oldRefreshToken) {
        RefreshToken savedToken = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new ResourceBadRequestException("Invalid Refresh Token"));

        if (savedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(savedToken);
            throw new ResourceForbiddenException("Refresh token expired. Please log in again.");
        }

        User user = savedToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        saveNewRefreshToken(user, newRefreshToken); // handles cleanup

        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, newRefreshToken));
    }

    /**
     * Logs out the user by deleting their refresh token. The token is located
     * using the string passed in the request.
     *
     * @param request the logout request containing the refresh token to delete
     * @throws ResourceBadRequestException if the token is invalid
     */
    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        String token = request.refreshToken();
        RefreshToken savedToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceBadRequestException("Invalid Refresh Token"));

        User user = savedToken.getUser();

        // If the token is already invalidated or the user is already logged out
        if (user.getRefreshToken() == null) {
            throw new ResourceBadRequestException("The provided refresh token has already been invalidated or the user is already logged out.");
        }

        // Break the link
        user.setRefreshToken(null);

        // Persist the unlink
        repo.saveAndFlush(user);  // ensure DB sees the unlink and triggers orphan removal
    }
}
