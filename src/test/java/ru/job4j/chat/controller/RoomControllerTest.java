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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.chat.ChatApplication;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.model.dto.RoomDTO;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApplication.class)
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private PersonService personService;

    private final ObjectMapper jsonConverter = new ObjectMapper();

    @Test
    @WithMockUser
    public void whenCreateAndUserNotFound() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("room");
        Room room = new Room(roomDTO.getName());
        Mockito.when(personService.findById(1)).thenReturn(Optional.empty());
        mvc.perform(
                post("/person/1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(roomDTO).getBytes())
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void whenSuccessCreate() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("room");
        Room room = new Room(roomDTO.getName());
        Person person = new Person("name", "email", "password");
        room.setCreator(person);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.save(room)).thenReturn(room);
        mvc.perform(
                post("/person/1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(roomDTO).getBytes())
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.creator.name", is(person.getName())))
                .andExpect(jsonPath("$.creator.email", is(person.getEmail())))
                .andExpect(jsonPath("$.creator.password", is(person.getPassword())));
    }

    @Test
    @WithMockUser
    public void whenPut() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("updated");
        Room room = new Room("room");
        Person person = new Person("name", "email", "password");
        room.setCreator(person);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        room.setName(roomDTO.getName());
        Mockito.when(roomService.save(room)).thenReturn(room);
        mvc.perform(
                put("/person/1/room/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(roomDTO).getBytes())
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        Room room = new Room("room");
        Person person = new Person("name", "email", "password");
        room.setCreator(person);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        mvc.perform(
                delete("/person/1/room/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenGetById() throws Exception {
        Room room = new Room("room");
        Person person = new Person("name", "email", "password");
        room.setCreator(person);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        mvc.perform(
                get("/person/1/room/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.creator.name", is(person.getName())))
                .andExpect(jsonPath("$.creator.email", is(person.getEmail())))
                .andExpect(jsonPath("$.creator.password", is(person.getPassword())));

    }

    @Test
    @WithMockUser
    public void whenGetUserCreatedRooms() throws Exception {
        Person person = new Person("name", "email", "password");
        Room room1 = new Room("room1", person);
        Room room2 = new Room("room2", person);
        Room room3 = new Room("room3", person);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        List<Room> rooms = Arrays.asList(room1, room2, room3);
        Mockito.when(roomService.findByCreator(person)).thenReturn(rooms);
        mvc.perform(
                get("/person/1/room"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].name", is(room1.getName())))
                .andExpect(jsonPath("$[0].creator.name", is(person.getName())))
                .andExpect(jsonPath("$[0].creator.email", is(person.getEmail())))
                .andExpect(jsonPath("$[0].creator.password", is(person.getPassword())))

                .andExpect(jsonPath("$[1].name", is(room2.getName())))
                .andExpect(jsonPath("$[1].creator.name", is(person.getName())))
                .andExpect(jsonPath("$[1].creator.email", is(person.getEmail())))
                .andExpect(jsonPath("$[1].creator.password", is(person.getPassword())))

                .andExpect(jsonPath("$[2].name", is(room3.getName())))
                .andExpect(jsonPath("$[2].creator.name", is(person.getName())))
                .andExpect(jsonPath("$[2].creator.email", is(person.getEmail())))
                .andExpect(jsonPath("$[2].creator.password", is(person.getPassword())));
    }

    @Test
    @WithMockUser
    public void whenAddPersonToRoom() throws Exception {
        Room room = new Room("room");
        room.setPersons(new HashSet<>());
        Person person = new Person("name", "email", "password");
        person.setRooms(new HashSet<>());
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        mvc.perform(
                put("/person/1/room/1/add"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void whenDeletePersonFromRoom() throws Exception {
        Room room = new Room("room");
        room.setPersons(new HashSet<>());
        Person person = new Person("name", "email", "password");
        person.setRooms(new HashSet<>());
        room.addPerson(person);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        mvc.perform(
                delete("/person/1/room/1/delete"))
                .andExpect(status().isOk());
    }
}