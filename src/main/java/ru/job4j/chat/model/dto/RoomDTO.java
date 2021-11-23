package ru.job4j.chat.model.dto;

import javax.validation.constraints.NotBlank;

public class RoomDTO {

    @NotBlank(message = "Name must be not empty")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
