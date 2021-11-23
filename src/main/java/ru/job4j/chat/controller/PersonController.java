package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.controller.exception.ObjectNotFoundException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.model.dto.PersonDTO;
import ru.job4j.chat.model.validation.Operation;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

import javax.security.auth.login.AccountException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> savePerson(@Valid @RequestBody PersonDTO personDTO) {
        if (personService.findByEmail(personDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Person with this email already exist.");
        }
        Role roleDB = roleService.findById(personDTO.getRoleId())
                .orElseThrow(() -> new ObjectNotFoundException(Role.class));
        Person person = new Person();
        person.setRole(roleDB);
        person.setPassword(encoder.encode(personDTO.getPassword()));
        person.setName(personDTO.getName());
        person.setEmail(personDTO.getEmail());
        return new ResponseEntity<>(personService.save(person), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<Person> editPerson(@RequestBody PersonDTO personDTO,
                                             @PathVariable int id) {
        if (personDTO.getName() == null
                && personDTO.getPassword() == null
                && personDTO.getEmail() == null
                && personDTO.getRoleId() == 0) {
            throw new NullPointerException("Name or pass or email or role mustn't be empty");
        }
        if (personDTO.getPassword() != null) {
            personDTO.setPassword(encoder.encode(personDTO.getPassword()));
        }
        Person personDB = personService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        return new ResponseEntity<>(
                personService.patch(personDB, personDTO),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> updatePerson(@Valid @RequestBody PersonDTO personDTO,
                                             @PathVariable int id) {
        Person personDB = personService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        Role roleDB = roleService.findById(personDTO.getRoleId())
                .orElseThrow(() -> new ObjectNotFoundException(Role.class));
        personDB.setEmail(personDTO.getEmail());
        personDB.setName(personDTO.getName());
        personDB.setPassword(personDTO.getPassword());
        personDB.setRole(roleDB);
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
