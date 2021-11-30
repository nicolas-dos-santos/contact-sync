package com.trio.contactsync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trio.contactsync.controllers.SyncController;
import com.trio.contactsync.model.SyncResponse;
import com.trio.contactsync.model.SyncResult;
import com.trio.contactsync.service.SyncService;
import com.trio.contactsync.util.ObjectCreator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@WebMvcTest(controllers = SyncController.class)
public class SyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SyncService service;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test
    public void shouldProcessAllContactsSuccessfully(){
        when(service.syncContacts()).thenReturn(SyncResult.builder()
                .withSuccessfulContacts(ObjectCreator.getList(ObjectCreator.getContact("a@a.com")))
                .build()
        );

        MvcResult result = mockMvc.perform(get("/contacts/sync"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = result.getResponse().getContentAsString();
        SyncResponse responseObject = objectMapper.readValue(actualResponseBody, SyncResponse.class);

        assertEquals("Wrong number of processed contacts", 1, responseObject.getSyncedContacts());
        assertNull("Should be null", responseObject.getContactErrors());
        assertNull("Should be null", responseObject.getGeneralError());
        assertEquals("Wrong number of contacts", 1, responseObject.getContacts().size());
        assertEquals("Wrong email","a@a.com", responseObject.getContacts().get(0).getEmail());

    }

    @SneakyThrows
    @Test
    public void shouldProcessContacts_SomeErrors(){
        when(service.syncContacts()).thenReturn(SyncResult.builder()
                .withSuccessfulContacts(ObjectCreator.getList(ObjectCreator.getContact("a@a.com")))
                .withContactErrors(ObjectCreator.getList(ObjectCreator.getContactError("b.com", "invalid email")))
                .build()
        );

        MvcResult result = mockMvc.perform(get("/contacts/sync"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = result.getResponse().getContentAsString();
        SyncResponse responseObject = objectMapper.readValue(actualResponseBody, SyncResponse.class);

        assertEquals("Wrong number of processed contacts", 1, responseObject.getSyncedContacts());
        assertEquals("Wrong number of processed contacts", 1, responseObject.getContactErrors().size());
        assertNull("Should be null", responseObject.getGeneralError());
        assertEquals("Wrong number of contacts", 1, responseObject.getContacts().size());
        assertEquals("Wrong email","a@a.com", responseObject.getContacts().get(0).getEmail());
        assertEquals("Wrong email","b.com", responseObject.getContactErrors().get(0).getContact().getEmail());
        assertEquals("Wrong message","invalid email", responseObject.getContactErrors().get(0).getErrorMessage());

    }

    @SneakyThrows
    @Test
    public void shouldProcessContacts_GeneralError(){
        when(service.syncContacts()).thenReturn(SyncResult.builder()
                .withGeneralError("mockApi is not available")
                .build()
        );

        MvcResult result = mockMvc.perform(get("/contacts/sync"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = result.getResponse().getContentAsString();
        SyncResponse responseObject = objectMapper.readValue(actualResponseBody, SyncResponse.class);

        assertNull("Should be null", responseObject.getContacts());
        assertNull("Should be null", responseObject.getContactErrors());
        assertEquals("Wrong number of contacts", 0, responseObject.getSyncedContacts());
        assertEquals("Wrong message","mockApi is not available", responseObject.getGeneralError());


    }
}
