package com.trio.contactsync.publisher;

import com.trio.contactsync.model.Member;
import com.trio.contactsync.util.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@TestPropertySource(properties={"publisher.mailchimp.base_address=localhost", "publisher.mailchimp.api_key=someKey",
        "publisher.mailchimp.list_id=12345"})
public class MailChimpPublisherTests {

    private final String localhost = "localhost";

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    private MailChimpPublisher publisher;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        when(restTemplate.exchange(startsWith(localhost), any(), any(), eq(String.class))).thenReturn(new ResponseEntity("", HttpStatus.OK));

    }

    @Test
    public void shouldPublishMembers() {
        List<Member> input = ObjectCreator.getList(ObjectCreator.getMember("a@a.com"), ObjectCreator.getMember("b@b.com"));
        PublishResult result = publisher.publish(input);

        assertEquals("Wrong number of members published", 2, result.getMembersPublished().size());
        assertEquals("Wrong email", "a@a.com", result.getMembersPublished().get(0).getEmail());
        assertEquals("Wrong email", "b@b.com", result.getMembersPublished().get(1).getEmail());
    }

    @Test
    public void shouldReturnErrorsInCaseOfIssues(){
        when(restTemplate.exchange(startsWith(localhost), any(), any(), eq(String.class))).then(args ->{
            String body = args.getArgument(2).toString();
            if(body.contains("a@a.com")){
                return new ResponseEntity("some error", HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity("", HttpStatus.OK);
            }
        });

        List<Member> input = ObjectCreator.getList(ObjectCreator.getMember("a@a.com"),
                ObjectCreator.getMember("b@b.com"), ObjectCreator.getMember("c@c.com"));
        PublishResult result = publisher.publish(input);

        assertEquals("Wrong number of members published", 2, result.getMembersPublished().size());
        assertEquals("Wrong number of errors", 1, result.getContactErrors().size());
        assertEquals("Wrong email", "b@b.com", result.getMembersPublished().get(0).getEmail());
        assertEquals("Wrong email", "c@c.com", result.getMembersPublished().get(1).getEmail());
        assertEquals("Wrong email", "a@a.com", result.getContactErrors().get(0).getContact().getEmail());

    }

}
