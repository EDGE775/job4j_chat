package ru.job4j.chat.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ObjectNotFoundException extends ResponseStatusException {
    public ObjectNotFoundException(Class cl) {
        super(HttpStatus.NOT_FOUND, String.format("%s not found.", cl.getSimpleName()));
    }
}
