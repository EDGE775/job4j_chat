package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.controller.exception.ObjectNotFoundException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.model.dto.RoomDTO;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import javax.validation.Valid;
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
        Person person = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        return new ResponseEntity<>(roomService.findByCreator(person), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int personId,
                                         @PathVariable int id) {
        Person person = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Room room = roomService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Room.class));
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@PathVariable int personId,
                                           @Valid @RequestBody RoomDTO roomDTO) {
        Person person = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Room room = new Room();
        room.setCreator(person);
        room.setName(roomDTO.getName());
        return new ResponseEntity<>(roomService.save(room), HttpStatus.CREATED);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Void> updateRoom(@PathVariable int personId,
                                           @PathVariable int roomId,
                                           @Valid @RequestBody RoomDTO roomDTO) {
        Person person = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Room roomDB = roomService.findById(roomId)
                .orElseThrow(() -> new ObjectNotFoundException(Room.class));
        roomDB.setName(roomDTO.getName());
        roomService.save(roomDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int personId,
                                           @PathVariable int roomId) {
        Person person = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Room roomDB = roomService.findById(roomId)
                .orElseThrow(() -> new ObjectNotFoundException(Room.class));
        roomService.delete(roomDB);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/add")
    public ResponseEntity<Void> addPersonToRoom(@PathVariable int personId,
                                                @PathVariable int roomId) {
        Person personDB = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Room roomDB = roomService.findById(roomId)
                .orElseThrow(() -> new ObjectNotFoundException(Room.class));
        roomDB.addPerson(personDB);
        roomService.save(roomDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}/delete")
    public ResponseEntity<Void> deletePersonFromRoom(@PathVariable int personId,
                                                     @PathVariable int roomId) {
        Person personDB = personService.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Room roomDB = roomService.findById(roomId)
                .orElseThrow(() -> new ObjectNotFoundException(Room.class));
        roomDB.removePerson(personDB);
        roomService.save(roomDB);
        return ResponseEntity.ok().build();
    }
}
