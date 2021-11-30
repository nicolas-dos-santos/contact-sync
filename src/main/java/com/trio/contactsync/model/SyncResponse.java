package com.trio.contactsync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
public class SyncResponse {
    private int syncedContacts; //long?
    private List<Contact> contacts;
    private String generalError;
    @JsonProperty("errors")
    private List<ContactError> contactErrors;
}
