package com.trio.contactsync.controllers;

import com.trio.contactsync.model.SyncResponse;
import com.trio.contactsync.model.SyncResult;
import com.trio.contactsync.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {

    @Autowired
    private SyncService syncService;

    @GetMapping(path = "contacts/sync")
    public SyncResponse syncContacts(){
        SyncResult syncResult = syncService.syncContacts();

        return SyncResponse.builder()
                .withContacts(syncResult.getSuccessfulContacts())
                .withContactErrors(syncResult.getContactErrors())
                .withSyncedContacts(syncResult.getSuccessfulContacts() != null ? syncResult.getSuccessfulContacts().size() : 0)
                .withGeneralError(syncResult.getGeneralError())
                .build();
    }
}
