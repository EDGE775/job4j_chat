package ru.job4j.chat.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
public class MessageRepositoryTest {

    @Autowired
    private PersonRepository personDB;

    @Autowired
    private RoomRepository roomDB;

    @Autowired
    private MessageRepository messageDB;

    @Test
    public void whenCreate() {
        Person author = personDB.save(personDB.save(new Person(1)));
        Room room = roomDB.save(roomDB.save(new Room(1)));
        Message message = new Message("msg", author, room);
        messageDB.save(message);
        Assert.assertEquals(message, messageDB.findById(message.getId()).orElse(new Message()));
    }

    @Test
    public void whenUpdate() {
        Person author = personDB.save(personDB.save(new Person(1)));
        Room room = roomDB.save(roomDB.save(new Room(1)));
        Message message = new Message("msg", author, room);
        messageDB.save(message);
        Message updated = new Message("msg2", author, room);
        updated.setId(message.getId());
        messageDB.save(updated);
        Assert.assertEquals(
                updated,
                messageDB.findById(message.getId()).orElse(new Message())
        );
    }

    @Test
    public void whenDelete() {
        Person author = personDB.save(personDB.save(new Person(1)));
        Room room = roomDB.save(roomDB.save(new Room(1)));
        Message message = new Message("msg", author, room);
        messageDB.save(message);
        messageDB.deleteById(message.getId());
        Assert.assertEquals(
                Optional.empty(),
                messageDB.findById(message.getId())
        );
    }

    @Test
    public void whenFindAll() {
        Person author = personDB.save(personDB.save(new Person(1)));
        Room room = roomDB.save(roomDB.save(new Room(1)));
        Message message1 = new Message("msg1", author, room);
        Message message2 = new Message("msg2", author, room);
        Message message3 = new Message("msg3", author, room);
        List<Message> messages = List.of(message1, message2, message3);
        messageDB.saveAll(messages);
        Assert.assertEquals(
                messages,
                messageDB.findAll()
        );
    }

    @Test
    public void whenFindByRoom() {
        Person author = personDB.save(personDB.save(new Person(1)));
        Room room = roomDB.save(roomDB.save(new Room(1)));
        Message message1 = new Message("msg1", author, room);
        Message message2 = new Message("msg2", author, room);
        Message message3 = new Message("msg3", author, room);
        List<Message> messages = List.of(message1, message2, message3);
        messageDB.saveAll(messages);
        Assert.assertEquals(
                messages,
                messageDB.findByRoom(room)
        );
    }

    @Test
    public void wheDeleteByRoom() {
        Person author = personDB.save(personDB.save(new Person(1)));
        Room room = roomDB.save(roomDB.save(new Room(1)));
        Message message1 = new Message("msg1", author, room);
        Message message2 = new Message("msg2", author, room);
        Message message3 = new Message("msg3", author, room);
        List<Message> messages = List.of(message1, message2, message3);
        messageDB.saveAll(messages);
        messageDB.deleteMessagesByRoom(room);
        Assert.assertEquals(List.of(), messageDB.findByRoom(room));
    }
}