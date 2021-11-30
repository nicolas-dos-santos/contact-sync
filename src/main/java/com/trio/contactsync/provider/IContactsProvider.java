package com.trio.contactsync.provider;

import com.trio.contactsync.exceptions.ProviderException;
import com.trio.contactsync.model.Contact;

import java.util.List;

public interface IContactsProvider {
    List<Contact> getContacts() throws ProviderException;
}
