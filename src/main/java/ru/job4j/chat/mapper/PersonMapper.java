package ru.job4j.chat.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.dto.PersonDTO;
import ru.job4j.chat.service.RoleService;

public class PersonMapper {

    public static Person mapPersonDtoToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setEmail(personDTO.getEmail());
        person.setPassword(personDTO.getPassword());
        person.setRole(new Role(personDTO.getRoleId()));
        return person;
    }

    public static PersonDTO mapPersonToPersonDto(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(person.getName());
        personDTO.setEmail(person.getEmail());
        personDTO.setPassword(person.getPassword());
        personDTO.setRoleId(person.getRole().getId());
        return personDTO;
    }
}
