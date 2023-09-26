package ru.skypro.flea.exception;

public class UserNotFoundError extends Error {

    public UserNotFoundError(String message) {
        super(message);
    }

}
