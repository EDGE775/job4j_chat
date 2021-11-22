package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.controller.exception.ObjectNotFoundException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

import javax.security.auth.login.AccountException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    private final RoleService roleService;

    private BCryptPasswordEncoder encoder;

    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    public PersonController(PersonService personService, RoleService roleService,
                            BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.personService = personService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Person person = personService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> savePerson(@RequestBody Person person,
                                             @RequestParam int role) {
        if (person.getName() == null
                || person.getPassword() == null
                || person.getEmail() == null) {
            throw new NullPointerException("Name, pass and email mustn't be empty");
        }
        if (personService.findByEmail(person.getEmail()) != null) {
            throw new IllegalArgumentException("Person with this email already exist.");
        }
        Role roleDB = roleService.findById(role)
                .orElseThrow(() -> new ObjectNotFoundException(Role.class));
        person.setRole(roleDB);
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                personService.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person,
                                             @PathVariable int id) {
        if (person.getName() == null
                || person.getPassword() == null
                || person.getEmail() == null) {
            throw new NullPointerException("Name, pass and email mustn't be empty");
        }
        Person personDB = personService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        personDB.setEmail(person.getEmail());
        personDB.setName(person.getName());
        personDB.setPassword(person.getPassword());
        personService.save(personDB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person personDB = personService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        personService.delete(personDB);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }
}
