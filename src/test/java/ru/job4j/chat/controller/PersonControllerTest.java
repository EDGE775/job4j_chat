package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.chat.ChatApplication;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.dto.PersonDTO;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.job4j.chat.mapper.PersonMapper.mapPersonDtoToPerson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private RoleService roleService;

    private final ObjectMapper jsonConverter = new ObjectMapper();

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void whenPost() throws Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("name");
        personDTO.setEmail("email");
        personDTO.setPassword("password");
        personDTO.setRoleId(1);
        Person person = mapPersonDtoToPerson(personDTO);
        Mockito.when(bCryptPasswordEncoder.encode(personDTO.getPassword())).thenReturn(personDTO.getPassword());
        Mockito.when(personService.findByEmail(personDTO.getEmail())).thenReturn(null);
        Mockito.when(roleService.findById(personDTO.getRoleId())).thenReturn(Optional.of(person.getRole()));
        Mockito.when(personService.save(person)).thenReturn(person);
        mvc.perform(
                post("/person/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(personDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(personDTO.getName())))
                .andExpect(jsonPath("$.email", is(personDTO.getEmail())))
                .andExpect(jsonPath("$.password", is(personDTO.getPassword())))
                .andExpect(jsonPath("$.role.id", is(personDTO.getRoleId())));
    }

    @Test
    @WithMockUser
    public void whenPut() throws Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("name");
        personDTO.setEmail("email");
        personDTO.setPassword("password");
        personDTO.setRoleId(1);
        Person person = mapPersonDtoToPerson(personDTO);
        Mockito.when(personService.findById(Mockito.anyInt())).thenReturn(Optional.of(person));
        Mockito.when(roleService.findById(Mockito.anyInt())).thenReturn(Optional.of(person.getRole()));
        Mockito.when(personService.save(person)).thenReturn(person);
        mvc.perform(
                put("/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(person))
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        Person person = new Person("name", "email", "password");
        int id = 1;
        Mockito.when(personService.findById(id)).thenReturn(Optional.of(person));
        mvc.perform(
                delete("/person/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenGetById() throws Exception {
        Person person = new Person("name", "email", "password");
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        mvc.perform(
                get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(person.getName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())))
                .andExpect(jsonPath("$.password", is(person.getPassword())));
    }

    @Test
    @WithMockUser
    public void whenGetAll() throws Exception {
        Person person1 = new Person("name1", "email1", "password1");
        Person person2 = new Person("name2", "email2", "password2");
        Mockito.when(personService.findAll()).thenReturn(List.of(person1, person2));
        mvc.perform(
                get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(person1.getName())))
                .andExpect(jsonPath("$[0].email", is(person1.getEmail())))
                .andExpect(jsonPath("$[0].password", is(person1.getPassword())))
                .andExpect(jsonPath("$[1].name", is(person2.getName())))
                .andExpect(jsonPath("$[1].email", is(person2.getEmail())))
                .andExpect(jsonPath("$[1].password", is(person2.getPassword())));
    }
}