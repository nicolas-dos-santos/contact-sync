package com.trio.contactsync.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Contact {
    @EqualsAndHashCode.Exclude
    private String firstName;
    @EqualsAndHashCode.Exclude
    private String lastName;

    @EqualsAndHashCode.Include
    private String email;


}
