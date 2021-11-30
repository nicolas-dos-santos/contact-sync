package com.trio.contactsync.publisher;

import com.trio.contactsync.model.ContactError;
import com.trio.contactsync.model.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
public class PublishResult {
    private List<Member> membersPublished;
    private List<ContactError> contactErrors;
}
