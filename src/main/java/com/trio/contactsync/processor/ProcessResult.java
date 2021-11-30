package com.trio.contactsync.processor;

import com.trio.contactsync.model.ContactError;
import com.trio.contactsync.model.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
public class ProcessResult {
    private List<Member> members;
    private List<ContactError> contactErrors;
}
