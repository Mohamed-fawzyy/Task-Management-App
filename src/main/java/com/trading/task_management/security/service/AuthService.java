package com.trading.task_management.security.service;

import com.trading.task_management.security.dto.AuthenticationRequest;
import com.trading.task_management.security.dto.AuthenticationResponse;
import com.trading.task_management.security.dto.LogoutRequest;
import com.trading.task_management.security.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<AuthenticationResponse> register(RegisterRequest registerRequest);
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest);
    ResponseEntity<AuthenticationResponse> refreshAccessToken(String token);

    void logout(LogoutRequest request);
}
