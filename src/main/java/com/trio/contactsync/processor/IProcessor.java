package com.trio.contactsync.processor;

import com.trio.contactsync.model.Contact;

import java.util.List;

public interface IProcessor {
    ProcessResult process(List<Contact> contacts);
}
