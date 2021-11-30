package com.trio.contactsync.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
public class SyncResult {
    private List<Contact> successfulContacts;
    private List<ContactError> contactErrors;
    private String generalError;

}
