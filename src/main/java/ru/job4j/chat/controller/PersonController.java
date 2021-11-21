package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    private final RoleService roleService;

    public PersonController(PersonService personService, RoleService roleService) {
        this.personService = personService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = personService.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/reg")
    public ResponseEntity<Person> savePerson(@RequestBody Person person,
                                             @RequestParam int role) {
        Role roleDB = roleService.findById(role).get();
        person.setRole(roleDB);
        return new ResponseEntity<>(
                personService.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person,
                                             @PathVariable int id) {
        Person personDB = personService.findById(id).get();
        personDB.setEmail(person.getEmail());
        personDB.setName(person.getName());
        personDB.setPassword(person.getPassword());
        personService.save(personDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person personDB = personService.findById(id).get();
        personService.delete(personDB);
        return ResponseEntity.ok().build();
    }
}
