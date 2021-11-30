package com.trio.contactsync.publisher;

import com.trio.contactsync.model.Member;

import java.util.List;

public interface IMemberPublisher {
    PublishResult publish(List<Member> members);
}
