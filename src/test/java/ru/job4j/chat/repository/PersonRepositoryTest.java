package ru.job4j.chat.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.chat.model.Person;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personDB;

    @Autowired
    private RoomRepository roomDB;

    @Test
    public void whenCreate() {
        Person person = new Person("test", "test@mail.ru", "123");
        personDB.save(person);
        Person result = personDB.findById(person.getId()).orElse(new Person());
        Assert.assertEquals(person, result);
    }

    @Test
    public void whenUpdate() {
        Person person = new Person("test", "test@mail.ru", "123");
        personDB.save(person);
        Person updated = new Person("test2", "test2@mail.ru", "1234");
        updated.setId(person.getId());
        personDB.save(updated);
        Person result = personDB.findById(person.getId()).orElse(new Person());
        Assert.assertEquals(updated, result);
    }

    @Test
    public void whenDelete() {
        Person person = new Person("test", "test@mail.ru", "123");
        personDB.save(person);
        personDB.deleteById(person.getId());
        Assert.assertEquals(
                Optional.empty(),
                personDB.findById(person.getId())
        );
    }

    @Test
    public void whenFindAll() {
        Person person1 = new Person("test", "test@mail.ru", "123");
        Person person2 = new Person("test", "test@mail.ru", "123");
        Person person3 = new Person("test", "test@mail.ru", "123");
        List<Person> input = List.of(person1, person2, person3);
        personDB.saveAll(input);
        Assert.assertEquals(
                List.of(person1, person2, person3),
                personDB.findAll()
        );
    }

    @Test
    public void findByName() {
        Person person = new Person("test", "test@mail.ru", "123");
        personDB.save(person);
        Person result = personDB.findByName(person.getName());
        Assert.assertEquals(person, result);
    }

    @Test
    public void findByEmail() {
        Person person = new Person("test", "test@mail.ru", "123");
        personDB.save(person);
        Person result = personDB.findByEmail(person.getEmail());
        Assert.assertEquals(person, result);
    }
}