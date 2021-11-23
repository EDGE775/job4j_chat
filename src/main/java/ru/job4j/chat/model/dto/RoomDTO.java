package ru.job4j.chat.model.dto;

public class RoomDTO {

    private String name;

    public RoomDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
