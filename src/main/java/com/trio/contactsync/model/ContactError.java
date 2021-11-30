package com.trio.contactsync.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ContactError {
    private Contact contact;
    private String errorMessage;
}
