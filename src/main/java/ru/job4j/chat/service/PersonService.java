package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
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
}
