package com.trio.contactsync.provider;

import com.trio.contactsync.exceptions.ProviderException;
import com.trio.contactsync.model.Contact;
import com.trio.contactsync.util.ObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.AssertionErrors;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@Slf4j
@SpringBootTest
@TestPropertySource(properties={"provider.mockapi.endpoint=localhost"})
public class MockApiProviderTests {

    private final String localhost = "localhost";

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    MockAPIContactProvider provider;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        Contact[] contacts = ObjectCreator.getContactsFromJson();

        when(restTemplate.getForEntity(eq(localhost), any())).thenReturn(new ResponseEntity(contacts, HttpStatus.OK));

    }


    @Test
    public void shouldGetContactsFromEndpoint(){
        try {
            List<Contact> contacts = provider.getContacts();
            AssertionErrors.assertTrue("contact list should have data", contacts.size() > 0);

        } catch (ProviderException e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
    }

    @Test
    public void shouldThrowExceptionInCaseOfError(){
        when(restTemplate.getForEntity(eq(localhost), any())).thenThrow(new RestClientException("Endpoint not found"));

        Exception exception = assertThrows(ProviderException.class, () ->{

            List<Contact> contacts = provider.getContacts();
        });

        String expectedMessage = "Error retrieving contacts from localhost. Cause: Endpoint not found";
        String actualMessage = exception.getMessage();

        assertEquals("Should be equal", expectedMessage, actualMessage);

    }

}
