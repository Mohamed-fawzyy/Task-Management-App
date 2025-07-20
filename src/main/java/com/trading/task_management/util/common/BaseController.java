package com.trading.task_management.util.common;

import com.trading.task_management.util.dto.CustomApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A base controller providing standard methods for building
 * consistent HTTP responses with {@link CustomApiResponse}.
 * <p>
 * This abstract class is intended to be extended by all REST controllers
 * to reduce repetition and promote uniform API responses.
 */
public abstract class BaseController {

    /**
     * Returns a 200 OK response with a success message and data payload.
     *
     * @param message A human-readable success message.
     * @param data    The actual data to be returned in the response body.
     * @param <T>     The type of data included in the response.
     * @return A ResponseEntity containing a {@link CustomApiResponse} with status 200.
     *
     * @apiNote Use this when the request was successful and you want to return some data.
     * Example: Returning a user DTO after a successful GET request.
     */
    protected <T> ResponseEntity<CustomApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(CustomApiResponse.of(HttpStatus.OK.value(), message, data));
    }

    /**
     * Returns a 200 OK response with only a success message (no data).
     *
     * @param message A human-readable success message.
     * @return A ResponseEntity containing a {@link CustomApiResponse} with status 200 and no body data.
     *
     * @apiNote Use this when the request was successful but no data needs to be returned.
     * Example: Logging out a user, verifying an email, or toggling a flag.
     */
    protected ResponseEntity<CustomApiResponse<Void>> ok(String message) {
        return ResponseEntity.ok(CustomApiResponse.of(HttpStatus.OK.value(), message));
    }

    /**
     * Returns a 201 Created response with a success message and data payload.
     *
     * @param message A human-readable message indicating successful creation.
     * @param data    The created resource data to return.
     * @param <T>     The type of data included in the response.
     * @return A ResponseEntity containing a {@link CustomApiResponse} with status 201.
     *
     * @apiNote Use this when a new resource has been successfully created (e.g., inserting a new record).
     * Example: Creating a new user or saving a new refresh token.
     */
    protected <T> ResponseEntity<CustomApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomApiResponse.of(HttpStatus.CREATED.value(), message, data));
    }

    /**
     * Returns a 201 Created response with a success message but no data.
     *
     * @param message A human-readable message indicating successful creation.
     * @return A ResponseEntity containing a {@link CustomApiResponse} with status 201 and no body data.
     *
     * @apiNote Use this when a new resource has been created but there is no need to return the object.
     * Example: A verification token has been generated and emailed, but not returned in the response.
     */
    protected ResponseEntity<CustomApiResponse<Void>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomApiResponse.of(HttpStatus.CREATED.value(), message));
    }
}
