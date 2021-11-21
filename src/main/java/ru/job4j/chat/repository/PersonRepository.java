package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findByName(String name);

}
