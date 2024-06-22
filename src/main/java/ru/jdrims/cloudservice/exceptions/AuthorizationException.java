package ru.jdrims.cloudservice.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("User is not authorized");
    }
}