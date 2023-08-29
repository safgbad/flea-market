package ru.skypro.flea.exception;

public class ResourceWithSpecifiedIdNotFoundException extends RuntimeException {

    public ResourceWithSpecifiedIdNotFoundException(String message) {
        super(message);
    }
}
