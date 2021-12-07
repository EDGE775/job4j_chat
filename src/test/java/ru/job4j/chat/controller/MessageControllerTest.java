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
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.model.dto.MessageDTO;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApplication.class)
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private PersonService personService;

    @MockBean
    private RoomService roomService;

    private final ObjectMapper jsonConverter = new ObjectMapper();

    @Test
    @WithMockUser
    public void whenCreateAndUserNotFound() throws Exception {
        Mockito.when(personService.findById(1)).thenReturn(Optional.empty());
        mvc.perform(post("/person/1/room/1/message")).andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreateAndRoomNotFound() throws Exception {
        Person person = new Person(1);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.empty());
        mvc.perform(post("/person/1/room/1/message")).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void whenSuccessCreate() throws Exception {
        Person person = new Person(1);
        Room room = new Room(1);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText("msg");
        Message message = new Message();
        message.setText(messageDTO.getText());
        message.setAuthor(person);
        message.setRoom(room);
        Mockito.when(messageService.save(message)).thenReturn(message);
        mvc.perform(
                post("/person/1/room/1/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(messageDTO))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text", is(message.getText())))
                .andExpect(jsonPath("$.author.id", is(person.getId())))
                .andExpect(jsonPath("$.room.id", is(room.getId())));
    }

    @Test
    @WithMockUser
    public void whenSuccessUpdate() throws Exception {
        Person person = new Person(1);
        Room room = new Room(1);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText("updated");
        Message message = new Message("msg");
        message.setAuthor(person);
        message.setRoom(room);
        message.setText(messageDTO.getText());
        Mockito.when(messageService.findById(1)).thenReturn(Optional.of(message));
        Mockito.when(messageService.save(message)).thenReturn(message);
        mvc.perform(
                put("/person/1/room/1/message?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.writeValueAsString(messageDTO))
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenSuccessDelete() throws Exception {
        Person person = new Person(1);
        Room room = new Room(1);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        Message message = new Message("msg");
        message.setAuthor(person);
        message.setRoom(room);
        Mockito.when(messageService.findById(1)).thenReturn(Optional.of(message));
        mvc.perform(
                delete("/person/1/room/1/message?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenSuccessGetByRoom() throws Exception {
        Person person = new Person(1);
        Room room = new Room(1);
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        Message message1 = new Message("msg1", person, room);
        Message message2 = new Message("msg2", person, room);
        Message message3 = new Message("msg3", person, room);
        List<Message> messages = List.of(message1, message2, message3);
        Mockito.when(messageService.findByRoom(room)).thenReturn(messages);
        mvc.perform(
                get("/person/1/room/1/message"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].text", is(message1.getText())))
                .andExpect(jsonPath("$.[0].author.id", is(person.getId())))
                .andExpect(jsonPath("$.[0].room.id", is(room.getId())))

                .andExpect(jsonPath("$.[1].text", is(message2.getText())))
                .andExpect(jsonPath("$.[1].author.id", is(person.getId())))
                .andExpect(jsonPath("$.[1].room.id", is(room.getId())))

                .andExpect(jsonPath("$.[2].text", is(message3.getText())))
                .andExpect(jsonPath("$.[2].author.id", is(person.getId())))
                .andExpect(jsonPath("$.[2].room.id", is(room.getId())));
    }

}