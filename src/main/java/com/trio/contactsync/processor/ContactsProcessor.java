package com.trio.contactsync.processor;

import com.trio.contactsync.model.Contact;
import com.trio.contactsync.model.ContactError;
import com.trio.contactsync.model.Member;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ContactsProcessor implements IProcessor {

    @Override
    public ProcessResult process(List<Contact> contacts) {
        List<ContactError> errors = new ArrayList<>();

        //using a set to remove duplicates
        Set<Contact> validContacts = new HashSet<>();

        contacts.stream().forEach(c ->{
            ContactError error = getContactError(c);
            if(error != null){
               errors.add(error);
            }else{
               validContacts.add(c);
            }
        });

        return ProcessResult.builder()
                .withContactErrors(errors)
                .withMembers(validContacts.stream()
                        .sorted(Comparator.comparing(Contact::getEmail)) //sort by email
                        .map(this::getMemberFromContact) //convert to member
                        .collect(Collectors.toList()))
                .build();
    }

    //contact is valid if it has a valid email (will use apache-commons to validate)
    private ContactError getContactError(Contact c){
        boolean valid = true;
        String errorMessage = null;
        if(c.getEmail() == null){
            valid = false;
            errorMessage = "Email is required";
        }
        if(valid && c.getEmail().trim().isEmpty()){
            valid = false;
            errorMessage = "Email can't be blank";
        }
        if(valid && !EmailValidator.getInstance().isValid(c.getEmail())){
            valid = false;
            errorMessage = "Invalid email";
        }

        if(!valid){
            return ContactError.builder()
                    .withContact(c)
                    .withErrorMessage(errorMessage)
                    .build();
        }
        return null;
    }

    private Member getMemberFromContact(Contact c){
        return Member.builder()
                .withEmail(c.getEmail())
                .withAssociatedContact(c)
                .withStatus("subscribed")
                .build();
    }
}
