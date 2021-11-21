package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/person/{personId}/room/{roomId}/message")
public class MessageController {

    private final MessageService messageService;

    private final PersonService personService;

    private final RoomService roomService;

    public MessageController(MessageService messageService,
                             PersonService personService,
                             RoomService roomService) {
        this.messageService = messageService;
        this.personService = personService;
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessagesByRoom(@PathVariable int personId,
                                                              @PathVariable int roomId) {
        Room room = roomService.findById(roomId).get();
        List<Message> messages = StreamSupport.stream(
                this.messageService.findByRoom(room).spliterator(), false)
                .collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@PathVariable int personId,
                                                 @PathVariable int roomId,
                                                 @RequestBody Message message) {
        Room room = roomService.findById(roomId).get();
        Person person = personService.findById(personId).get();
        message.setAuthor(person);
        message.setRoom(room);
        return new ResponseEntity<>(
                messageService.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<Void> updateMessage(@PathVariable int personId,
                                                 @PathVariable int roomId,
                                                 @RequestBody Message message,
                                                 @RequestParam int id) {
        Room room = roomService.findById(roomId).get();
        Person person = personService.findById(personId).get();
        Message messageDB = messageService.findById(id).get();
        messageDB.setAuthor(person);
        messageDB.setRoom(room);
        messageDB.setText(message.getText());
        messageService.save(messageDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable int personId,
                                       @PathVariable int roomId,
                                       @RequestParam int id) {
        Message message = messageService.findById(id).get();
        messageService.delete(message);
        return ResponseEntity.ok().build();
    }
}
