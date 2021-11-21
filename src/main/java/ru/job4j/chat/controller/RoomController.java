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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person/{personId}/room")
public class RoomController {

    private final RoomService roomService;

    private final PersonService personService;

    private final MessageService messageService;

    public RoomController(RoomService roomService, PersonService personService, MessageService messageService) {
        this.roomService = roomService;
        this.personService = personService;
        this.messageService = messageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms(@PathVariable int personId) {
        return new ResponseEntity<>(roomService.findAll(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRoomsByCreator(@PathVariable int personId) {
        Person person = personService.findById(personId).get();
        return new ResponseEntity<>(roomService.findByCreator(person), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int personId,
                                         @PathVariable int id) {
        var room = roomService.findById(id);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@PathVariable int personId,
                                           @RequestBody Room room) {
        Person person = personService.findById(personId).get();
        room.setCreator(person);
        return new ResponseEntity<>(
                roomService.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Void> updateRoom(@PathVariable int personId,
                                           @PathVariable int roomId,
                                           @RequestBody Room room) {
        Room roomDB = roomService.findById(roomId).get();
        roomDB.setName(room.getName());
        roomService.save(roomDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int personId,
                                           @PathVariable int roomId) {
        Room room = roomService.findById(roomId).get();
        roomService.delete(room);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/add")
    public ResponseEntity<Void> addPersonToRoom(@PathVariable int personId,
                                                @PathVariable int roomId) {
        Room roomDB = roomService.findById(roomId).get();
        Person personDB = personService.findById(personId).get();
        roomDB.addPerson(personDB);
        roomService.save(roomDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}/delete")
    public ResponseEntity<Void> deletePersonFromRoom(@PathVariable int personId,
                                                     @PathVariable int roomId) {
        Room roomDB = roomService.findById(roomId).get();
        Person personDB = personService.findById(personId).get();
        roomDB.removePerson(personDB);
        roomService.save(roomDB);
        return ResponseEntity.ok().build();
    }
}
