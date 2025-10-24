package com.pinosoft.micita.exception;

import org.springframework.http.HttpStatus;
public class CitaException extends RuntimeException {
	private final ErrorType errorType;
    private final HttpStatus httpStatus;

    // Tipos de errores predefinidos
    public enum ErrorType {
        RESOURCE_NOT_FOUND,
        BUSINESS_RULE_VIOLATION,
        AVAILABILITY_CONFLICT,
        OPERATION_NOT_ALLOWED,
        VALIDATION_ERROR,
        INTERNAL_ERROR
    }

    // Constructor base
    private CitaException(String message, ErrorType errorType, HttpStatus httpStatus) {
        super(message);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }

    // Métodos factory para crear diferentes tipos de excepciones
    public static CitaException resourceNotFound(String message) {
        return new CitaException(message, ErrorType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static CitaException businessRuleViolation(String message) {
        return new CitaException(message, ErrorType.BUSINESS_RULE_VIOLATION, HttpStatus.BAD_REQUEST);
    }

    public static CitaException availabilityConflict(String message) {
        return new CitaException(message, ErrorType.AVAILABILITY_CONFLICT, HttpStatus.CONFLICT);
    }

    public static CitaException operationNotAllowed(String message) {
        return new CitaException(message, ErrorType.OPERATION_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }

    public static CitaException validationError(String message) {
        return new CitaException(message, ErrorType.VALIDATION_ERROR, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // Getters
    public ErrorType getErrorType() {
        return errorType;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
