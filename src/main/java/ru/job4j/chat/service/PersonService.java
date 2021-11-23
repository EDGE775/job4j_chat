package ru.job4j.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.dto.PersonDTO;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    private final PersonRepository repository;

    private final RoleService roleService;

    public PersonService(PersonRepository repository,
                         RoleService roleService) {
        this.repository = repository;
        this.roleService = roleService;
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public List<Person> findAll() {
        return StreamSupport.stream(
                repository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Person save(Person person) {
        return repository.save(person);
    }

    public void delete(Person person) {
        repository.delete(person);
    }

    public Person findByName(String name) {
        return repository.findByName(name);
    }

    public Person findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Person patch(Person personDB, PersonDTO personDTO) {
        String name = personDTO.getName();
        String email = personDTO.getEmail();
        String password = personDTO.getPassword();
        int roleId = personDTO.getRoleId();
        if (!name.isBlank() && !Objects.equals(personDB.getName(), name)) {
            personDB.setName(name);
        }
        if (!email.isBlank() && !Objects.equals(personDB.getEmail(), email)) {
            personDB.setName(email);
        }
        if (!password.isBlank()) {
            personDB.setPassword(password);
        }
        if (roleId != 0 && roleId != personDB.getRole().getId()) {
            personDB.setRole(roleService.findById(roleId).get());
        }
        return save(personDB);
    }
}
