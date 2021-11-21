package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Integer> {

    List<Room> findByCreator(Person person);
}
