package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> findByRoom(Room room) {
        return repository.findByRoom(room);
    }

    public Message save(Message message) {
        return repository.save(message);
    }

    public Optional<Message> findById(int id) {
        return repository.findById(id);
    }

    public void delete(Message message) {
        repository.delete(message);
    }

    public void deleteMessagesByRoom(Room room) {
        repository.deleteMessagesByRoom(room);
    }
}
