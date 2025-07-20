package com.trading.task_management.security.controller;

import com.trading.task_management.security.dto.*;
import com.trading.task_management.security.service.AuthService;
import com.trading.task_management.util.common.BaseController;
import com.trading.task_management.util.dto.CustomApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@Tag(name = "Authentication", description = "APIs for user registration and authentication")
@RequestMapping("/api/task-management/auth")
public class AuthController extends BaseController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "This endpoint allows a new user to register by providing their details. No authentication is required.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Input data should follow the right format for strong pass and valid email and no null values.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class) // Specify the DTO schema
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully with a resource(code) but not verified yet.",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input: Missing or invalid fields in the request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example of an empty or null data if entered",
                                                    summary = "BadContentResponse",
                                                    value = "{\"firstName\":\"First name is required\",\"lastName\":\"Last name " +
                                                            "must contain only letters\",\"password\":\"Password must contain at" +
                                                            " least one letter, one number, and one special character\",\"email\":\"" +
                                                            "Invalid email format. Email must contain a valid domain like .com or .net\"}"
                                            ),
                                            @ExampleObject(
                                                    name = "Example of unknown fields at JSON request.",
                                                    summary = "Unknown fields are not allowed",
                                                    value = """
                                                            Invalid request: Unknown fields are not allowed.
                                                            """
                                            )}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: DUPLICATE data for this user is Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "This email was registered before with another user",
                                            summary = "Registered Email",
                                            value = """
                                                    {"timestamp":"2025-02-13T11:22:00.896+00:00",
                                                    "status":409,
                                                    "error":"Conflict",
                                                    "trace":"com.task-management.exceptions.DuplicateResourceException: Email already in use.",
                                                    "message":"Email already in use.",
                                                    "path":"/api/task-management/auth/v1/register"}
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error: Something went wrong on the server"
                    )
            }
    )
    @PostMapping("/v1/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @Operation(
            summary = "Authenticate a user",
            description = "This endpoint allows a user to authenticate by providing their credentials. No authentication is required.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Input data should be correct creds and no null values.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthenticationRequest.class) // Specify the DTO schema
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully and error will be NULL",
                            content = @Content(
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input: Missing or invalid fields in the request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example of data when email format is incorrect",
                                                    summary = "Invalid Email Format",
                                                    value = """
                                                            {
                                                                "email": "Invalid email format. Must contain a valid domain like .com or .net"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Example of data when password is too weak",
                                                    summary = "Weak Password",
                                                    value = """
                                                            {
                                                                "password": "Password must contain at least one letter, one number, and one special character"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Example of data when both email and password are null or empty",
                                                    summary = "Empty or Null Fields",
                                                    value = """
                                                            {
                                                                "email": "Email should not be empty or null",
                                                                "password": "Password should not be empty or null"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Example of unknown fields at JSON request.",
                                                    summary = "Unknown fields are not allowed",
                                                    value = """
                                                            Invalid request: Unknown fields are not allowed.
                                                            """
                                            )
                                    }

                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: Invalid credentials",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Example of registered user but wrong creds",
                                            summary = "Wrong Email or Pass",
                                            value = """
                                                    {
                                                        "timestamp": "2025-02-13T09:59:51.739+00:00",
                                                        "status": 401,
                                                        "error": "Not Auth",
                                                        "trace": "org.springframework...",
                                                        "message": "Invalid email or password.",
                                                        "path": "/api/task-management/auth/v1/authenticate"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND: User Resource Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Example of unregistered user.",
                                            summary = "Wrong Email or Pass",
                                            value =
                                                    """
                                                            {
                                                                "timestamp": "2025-02-13T09:59:51.739+00:00",
                                                                "status": 404,
                                                                "error": "Not Found",
                                                                "message": "User with this email is not found.",
                                                                "path": "/api/task-management/auth/v1/authenticate"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error: Something went wrong on the server"
                    )
            }
    )
    @PostMapping("/v1/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Get a new access token using a valid refresh token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The refresh token",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "New access token issued",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
            }
    )
    @PostMapping("/v1/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshAccessToken(request.refreshToken());
    }

    @Operation(
            summary = "Logout the user",
            description = "This endpoint allows a user to logout by invalidating their refresh token. After successful logout, the user will be logged out and their refresh token will no longer be valid.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body should contain a valid refresh token that needs to be invalidated.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LogoutRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logout successful. The refresh token has been invalidated.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Successful Logout",
                                            summary = "Example of a successful logout response",
                                            value = """
                                                    {
                                                        "message": "Logout successful."
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request: The refresh token provided is missing or malformed.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Invalid Refresh Token",
                                            summary = "Example of an invalid or malformed refresh token",
                                            value = """
                                                    {
                                                        "error": "Invalid refresh token format or missing token."
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: The provided refresh token is not valid or expired.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Expired or Invalid Token",
                                            summary = "Example of an expired or invalid refresh token",
                                            value = """
                                                    {
                                                        "error": "The provided refresh token has expired or is invalid."
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: The refresh token is already invalidated or has already been logged out.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Already Logged Out",
                                            summary = "Example of a conflict when the user is already logged out",
                                            value = """
                                                    {
                                                        "error": "The provided refresh token has already been invalidated or the user is already logged out."
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error: Something went wrong on the server.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Server Error",
                                            summary = "Example of a server error response",
                                            value = """
                                                    {
                                                        "timestamp": "2025-02-13T09:59:51.739+00:00",
                                                        "status": 500,
                                                        "error": "Internal Server Error",
                                                        "message": "An error occurred while processing the logout request.",
                                                        "path": "/api/auth/v1/logout"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/v1/logout")
    public ResponseEntity<CustomApiResponse<Void>> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ok("Logout successful.");
    }
}
