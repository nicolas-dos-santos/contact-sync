package com.trio.contactsync.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trio.contactsync.exceptions.PublisherException;
import com.trio.contactsync.model.ContactError;
import com.trio.contactsync.model.Member;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MailChimpPublisher implements IMemberPublisher {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${publisher.mailchimp.base_address}")
    private String baseUrl;

    @Value("${publisher.mailchimp.api_key}")
    private String apiKey;

    @Value("${publisher.mailchimp.list_id}")
    private String listId;


    @Override
    public PublishResult publish(List<Member> members){
        List<Member> membersAdded = new ArrayList<>();
        List<ContactError> errors = new ArrayList<>();

        members.parallelStream().forEach(m -> {
            try{
                addMemberToList(m);
                membersAdded.add(m);
            }catch (Exception e){
                errors.add(ContactError.builder().withContact(m.getAssociatedContact()).withErrorMessage(e.getMessage()).build());
            }
        });

        return PublishResult.builder()
                .withContactErrors(errors)
                .withMembersPublished(membersAdded.stream().sorted(Comparator.comparing(Member::getEmail)).collect(Collectors.toList()))
                .build();
    }

    private void addMemberToList(Member m) throws JsonProcessingException, PublisherException {
        HttpEntity httpEntity = getHttpEntity(m);
        String subscriberHash = getSubscriberHash(m.getEmail().toLowerCase());

        String endpoint = String.format("%s/lists/%s/members/%s", baseUrl, listId, subscriberHash);

        log.info("Sending body: {} to {}", httpEntity.getBody(), endpoint);

        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, httpEntity, String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            log.info("Success");
            log.info("Body: {}", response.getBody());
        }else{
            log.error("Error publishing member. Details: {}", response.getBody());
            throw new PublisherException(String.format("Error publishing member. Cause: %s", response.getBody()), null);
        }
    }

    private HttpEntity<String> getHttpEntity(Member m) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String authContent = String.format("Bearer %s", apiKey);
        headers.set("Authorization", authContent);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(m);
        return new HttpEntity<>(body, headers);
    }

    //the subscriberHash is the md5 hash of the email
    @SneakyThrows
    private String getSubscriberHash(String email){
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(email.getBytes());
        byte[] digest = md.digest();
        return HexUtils.toHexString(digest).toLowerCase();
    }
}
