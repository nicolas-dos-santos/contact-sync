package com.trio.contactsync.service;

import com.trio.contactsync.exceptions.ProviderException;
import com.trio.contactsync.model.Contact;
import com.trio.contactsync.model.ContactError;
import com.trio.contactsync.model.Member;
import com.trio.contactsync.model.SyncResult;
import com.trio.contactsync.processor.IProcessor;
import com.trio.contactsync.processor.ProcessResult;
import com.trio.contactsync.provider.IContactsProvider;
import com.trio.contactsync.publisher.IMemberPublisher;
import com.trio.contactsync.publisher.PublishResult;
import com.trio.contactsync.util.ObjectCreator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class SyncServiceTests {

    @Mock
    IContactsProvider provider;

    @Mock
    IProcessor processor;

    @Mock
    IMemberPublisher publisher;

    @InjectMocks
    @Autowired
    SyncService service;

    private final String INVALID_EMAIL_PROCESS = "asd.com";
    private final String INVALID_EMAIL_PUBLISH = "a@a.com";

    @SneakyThrows
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        when(processor.process(any())).then(args ->{
            List<Contact> allContacts = args.getArgument(0);

            List<ContactError> errors = allContacts.stream()
                    .filter(c-> c.getEmail().equals(INVALID_EMAIL_PROCESS))
                    .map(c-> ContactError.builder().withContact(c).withErrorMessage("invalid email").build())
                    .collect(Collectors.toList());

            return ProcessResult.builder()
                    .withMembers(allContacts.stream()
                            .filter(c-> !c.getEmail().equals(INVALID_EMAIL_PROCESS))
                            .map(c-> Member.builder()
                                    .withEmail(c.getEmail())
                                    .withAssociatedContact(c)
                                    .withStatus("subscribed").build())
                            .collect(Collectors.toList()))
                    .withContactErrors(errors)
                    .build();
        });

        when(publisher.publish(any())).then(args ->{
            List<Member> allMembers = args.getArgument(0);

            List<ContactError> errors = allMembers.stream()
                    .filter(m-> m.getEmail().equals(INVALID_EMAIL_PUBLISH))
                    .map(m-> ContactError.builder().withContact(m.getAssociatedContact()).withErrorMessage("email looks invalid").build())
                    .collect(Collectors.toList());

            return PublishResult.builder()
                    .withMembersPublished(allMembers.stream()
                            .filter(m-> !m.getEmail().equals(INVALID_EMAIL_PUBLISH))
                            .collect(Collectors.toList()))
                    .withContactErrors(errors)
                    .build();
        });
    }

    @SneakyThrows
    @Test
    public void shouldSyncContactsSuccessfully(){
        when(provider.getContacts()).thenReturn(ObjectCreator.getList(
                ObjectCreator.getContact("user@domain.com"), ObjectCreator.getContact("b@b.com")));

       SyncResult result = service.syncContacts();

        assertEquals("Wrong number of successful contacts", 2, result.getSuccessfulContacts().size());
        assertNull("General error should be null", result.getGeneralError());
        assertEquals("Wrong number of contact errors", 0, result.getContactErrors().size());

    }

    @SneakyThrows
    @Test
    public void shouldReturnValidAndErrorResults(){
        when(provider.getContacts()).thenReturn(ObjectCreator.getList(
                ObjectCreator.getContact("user@domain.com"),ObjectCreator.getContact("user2@domain.com"),
                ObjectCreator.getContact(INVALID_EMAIL_PROCESS), ObjectCreator.getContact(INVALID_EMAIL_PUBLISH)));

        SyncResult result = service.syncContacts();

        assertEquals("Wrong number of successful contacts", 2, result.getSuccessfulContacts().size());
        assertNull("General error should be null", result.getGeneralError());
        assertEquals("Wrong number of contact errors", 2, result.getContactErrors().size());
        assertEquals("Wrong email", INVALID_EMAIL_PROCESS, result.getContactErrors().get(0).getContact().getEmail());
        assertEquals("Wrong email", INVALID_EMAIL_PUBLISH, result.getContactErrors().get(1).getContact().getEmail());

    }

    @SneakyThrows
    @Test
    public void shouldReturnGeneralError(){
        when(provider.getContacts()).thenThrow(new ProviderException("some error", null));

        SyncResult result = service.syncContacts();

        assertNull("Should be null", result.getSuccessfulContacts());
        assertNull("Should be null", result.getContactErrors());
        assertNotNull("General error should not be null", result.getGeneralError());
        assertEquals("Wrong message", "some error", result.getGeneralError());

    }

}
