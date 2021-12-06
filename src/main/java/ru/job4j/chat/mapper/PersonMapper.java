package ru.job4j.chat.mapper;

import org.springframework.stereotype.Component;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.dto.PersonDTO;

@Component
public class PersonMapper {

    public Person mapPersonDtoToPerson(PersonDTO dto) {
        return null;
    }

    public PersonDTO mapPersonToPersonDto(Person person) {
        return null;
    }
}
