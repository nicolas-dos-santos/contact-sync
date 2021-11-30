package com.trio.contactsync.provider;

import com.trio.contactsync.exceptions.ProviderException;
import com.trio.contactsync.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class MockAPIContactProvider implements IContactsProvider {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${provider.mockapi.endpoint}")
    private String mockAPIEndpoint;

    @Override
    public List<Contact> getContacts() throws ProviderException {
        log.info("Getting contacts from: {}", mockAPIEndpoint);
        try{
            ResponseEntity<Contact[]> response = restTemplate.getForEntity(mockAPIEndpoint, Contact[].class);
            List<Contact> contacts = Arrays.asList(response.getBody());
            log.info("Got {} contacts from endpoint", contacts.size());
            return contacts;
        }catch (Exception e){
            String errorMessage = String.format("Error retrieving contacts from %s", mockAPIEndpoint);
            log.error(errorMessage, e);
            throw new ProviderException(errorMessage, e);
        }
    }
}
