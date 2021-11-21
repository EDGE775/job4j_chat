package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomService {

    private final RoomRepository repository;

    private final MessageService messageService;

    public RoomService(RoomRepository repository, MessageService messageService) {
        this.repository = repository;
        this.messageService = messageService;
    }

    public Optional<Room> findById(int id) {
        return repository.findById(id);
    }

    public Room save(Room room) {
        return repository.save(room);
    }

    public List<Room> findAll() {
        return StreamSupport.stream(
                repository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public List<Room> findByCreator(Person person) {
        return repository.findByCreator(person);
    }

    @Transactional
    public void delete(Room room) {
        messageService.deleteMessagesByRoom(room);
        repository.delete(room);
    }
}
