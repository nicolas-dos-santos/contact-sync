package com.trio.contactsync.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class Member {
    @JsonProperty("email_address")
    private String email;
    @JsonProperty("status_if_new")
    private String status;

    @JsonIgnore
    private Contact associatedContact;

    public String getEmail(){
        return this.associatedContact.getEmail();
    }
}
