package ru.job4j.chat.model.dto;

import javax.validation.constraints.NotBlank;

public class MessageDTO {

    @NotBlank(message = "Text must be not empty")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
