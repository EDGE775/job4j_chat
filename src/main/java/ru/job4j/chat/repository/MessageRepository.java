package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findByRoom(Room room);

    void deleteMessagesByRoom(Room room);
}
