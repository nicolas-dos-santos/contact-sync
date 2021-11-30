package com.trio.contactsync.processor;

import com.trio.contactsync.model.Contact;
import com.trio.contactsync.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ContactProcessorTests {

    @Autowired
    ContactsProcessor processor;

    @Test
    public void shouldProcessContactCorrectly(){
        List<Contact> inputList = ObjectCreator.getList(ObjectCreator.getContact("a@a.com"));

        ProcessResult processResult = processor.process(inputList);

        assertEquals(0, processResult.getContactErrors().size());
        assertEquals(1, processResult.getMembers().size());
        assertEquals("a@a.com", processResult.getMembers().get(0).getEmail());
    }

    @Test
    public void shouldReturnInvalidEmail_Null(){
        List<Contact> inputList = ObjectCreator.getList(ObjectCreator.getContact(null));

        ProcessResult processResult = processor.process(inputList);
        assertEquals(1, processResult.getContactErrors().size());
        assertEquals(0, processResult.getMembers().size());
        assertEquals("Email is required", processResult.getContactErrors().get(0).getErrorMessage());
    }

    @Test
    public void shouldReturnInvalidEmail_Blank(){
        List<Contact> inputList = ObjectCreator.getList(ObjectCreator.getContact(""), ObjectCreator.getContact("     "));

        ProcessResult processResult = processor.process(inputList);
        assertEquals(2, processResult.getContactErrors().size());
        assertEquals(0, processResult.getMembers().size());
        assertEquals("Email can't be blank", processResult.getContactErrors().get(0).getErrorMessage());
        assertEquals("Email can't be blank", processResult.getContactErrors().get(1).getErrorMessage());
    }

    @Test
    public void shouldReturnInvalidEmail_Invalid(){
        List<Contact> inputList = ObjectCreator.getList(ObjectCreator.getContact("invalidEmail"),
                ObjectCreator.getContact("a@a"), ObjectCreator.getContact("a.com"));

        ProcessResult processResult = processor.process(inputList);
        assertEquals(3, processResult.getContactErrors().size());
        assertEquals(0, processResult.getMembers().size());
        assertEquals("Invalid email", processResult.getContactErrors().get(0).getErrorMessage());
        assertEquals("Invalid email", processResult.getContactErrors().get(1).getErrorMessage());
        assertEquals("Invalid email", processResult.getContactErrors().get(2).getErrorMessage());
    }

    @Test
    public void shouldRemoveDuplicates(){
        List<Contact> inputList = ObjectCreator.getList(ObjectCreator.getContact("a@a.com"),
                ObjectCreator.getContact("b@b.com"), ObjectCreator.getContact("a@a.com"));

        ProcessResult processResult = processor.process(inputList);
        assertEquals(0, processResult.getContactErrors().size());
        assertEquals(2, processResult.getMembers().size());
        assertEquals("a@a.com", processResult.getMembers().get(0).getEmail());
        assertEquals("b@b.com", processResult.getMembers().get(1).getEmail());
    }

}
