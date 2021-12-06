package ru.job4j.chat.handlers;

public class BadResponse {

    private final String message;

    public BadResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
