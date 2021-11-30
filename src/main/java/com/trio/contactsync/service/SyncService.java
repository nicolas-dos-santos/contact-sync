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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SyncService {

    @Autowired
    private IContactsProvider contactsProvider;

    @Autowired
    private IProcessor processor;

    @Autowired
    private IMemberPublisher memberPublisher;

    public SyncResult syncContacts(){
        try{
            List<Contact> contactsToSync = contactsProvider.getContacts();
            ProcessResult processResult = processor.process(contactsToSync);

            PublishResult publishResult = memberPublisher.publish(processResult.getMembers());

            //errors from processing and publishing
            List<ContactError> contactErrors = Stream.concat(
                                                    processResult.getContactErrors().stream(),
                                                    publishResult.getContactErrors().stream()
                                                    ).collect(Collectors.toList());

            return SyncResult.builder()
                    .withContactErrors(contactErrors)
                    .withSuccessfulContacts(
                            publishResult.getMembersPublished().stream()
                                    .map(Member::getAssociatedContact)
                                    .collect(Collectors.toList()))
                    .build();
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
    }

    private SyncResult getErrorResult(String error){
        return SyncResult.builder()
                .withGeneralError(error)
                .build();
    }
}
