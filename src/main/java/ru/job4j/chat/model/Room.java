package ru.job4j.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Room() {
    }

    public Room(int id) {
        this.id = id;
    }

    public Room(String name) {
        this.name = name;
    }

    public Room(String name, Person creator) {
        this.name = name;
        this.creator = creator;
    }

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person creator;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "rooms_persons",
            joinColumns = {
                    @JoinColumn(name = "room_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "person_id")}
    )
    private Set<Person> persons;

    public void addPerson(Person person) {
        persons.add(person);
        person.addRoom(this);
    }

    public void removePerson(Person person) {
        persons.remove(person);
        person.getRooms().remove(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id
                && Objects.equals(name, room.name)
                && Objects.equals(creator, room.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator);
    }
}
