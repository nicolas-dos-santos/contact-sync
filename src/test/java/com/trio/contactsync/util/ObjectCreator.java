package com.trio.contactsync.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trio.contactsync.model.Contact;
import com.trio.contactsync.model.ContactError;
import com.trio.contactsync.model.Member;

import java.util.Arrays;
import java.util.List;

public class ObjectCreator {

    public static Contact getContact(String email){
        Contact c = new Contact();
        c.setEmail(email);
        return c;
    }

    public static Member getMember(String email){
        return Member.builder()
                .withAssociatedContact(getContact(email))
                .withEmail(email)
                .withStatus("subscribed")
                .build();
    }

    public static ContactError getContactError(String email, String errorMessage){
        return ContactError.builder()
                .withErrorMessage(errorMessage)
                .withContact(getContact(email))
                .build();
    }

    public static <T> List<T> getList(T... input){
        return Arrays.asList(input);
    }

    public static Contact[] getContactsFromJson(){
        String input = "[{\"createdAt\":\"2021-09-10T11:25:43.489Z\",\"firstName\":\"Mohammed\",\"lastName\":\"Koepp\",\"email\":\"Augustine62@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/ddggccaa_128.jpg\",\"id\":\"1\"},{\"createdAt\":\"2021-09-10T13:39:49.909Z\",\"firstName\":\"Adam\",\"lastName\":\"Rolfson\",\"email\":\"Zoie50@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/nicoleglynn_128.jpg\",\"id\":\"2\"},{\"createdAt\":\"2021-09-10T15:56:05.231Z\",\"firstName\":\"Norene\",\"lastName\":\"Effertz\",\"email\":\"Jazmyn15@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/rodnylobos_128.jpg\",\"id\":\"3\"},{\"createdAt\":\"2021-09-10T12:48:00.087Z\",\"firstName\":\"Anibal\",\"lastName\":\"Deckow\",\"email\":\"Brandyn83@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/m4rio_128.jpg\",\"id\":\"4\"},{\"createdAt\":\"2021-09-10T04:56:34.651Z\",\"firstName\":\"Theresa\",\"lastName\":\"Gottlieb\",\"email\":\"Pamela.Johnston76@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/prrstn_128.jpg\",\"id\":\"5\"},{\"createdAt\":\"2021-09-10T05:09:31.253Z\",\"firstName\":\"Alisa\",\"lastName\":\"Lebsack\",\"email\":\"Clotilde14@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/helderleal_128.jpg\",\"id\":\"6\"},{\"createdAt\":\"2021-09-09T21:22:52.978Z\",\"firstName\":\"Idell\",\"lastName\":\"Wisozk\",\"email\":\"Gudrun_Langosh4@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/baumannzone_128.jpg\",\"id\":\"7\"},{\"createdAt\":\"2021-09-09T23:22:47.548Z\",\"firstName\":\"Kyla\",\"lastName\":\"Kemmer\",\"email\":\"Ezra37@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/terryxlife_128.jpg\",\"id\":\"8\"},{\"createdAt\":\"2021-09-10T01:12:18.933Z\",\"firstName\":\"Damian\",\"lastName\":\"Upton\",\"email\":\"Patience26@hotmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/nicklacke_128.jpg\",\"id\":\"9\"},{\"createdAt\":\"2021-09-10T02:49:37.753Z\",\"firstName\":\"Daniella\",\"lastName\":\"Davis\",\"email\":\"Albert_Sauer@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/marciotoledo_128.jpg\",\"id\":\"10\"},{\"createdAt\":\"2021-09-10T11:38:00.321Z\",\"firstName\":\"Walker\",\"lastName\":\"Bednar\",\"email\":\"Toby39@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/laasli_128.jpg\",\"id\":\"11\"},{\"createdAt\":\"2021-09-10T08:53:54.668Z\",\"firstName\":\"Marcel\",\"lastName\":\"Mayer\",\"email\":\"Frida_Orn85@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/matt3224_128.jpg\",\"id\":\"12\"},{\"createdAt\":\"2021-09-10T00:04:40.855Z\",\"firstName\":\"Francis\",\"lastName\":\"Bailey\",\"email\":\"Wayne_Wolff13@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/samscouto_128.jpg\",\"id\":\"13\"},{\"createdAt\":\"2021-09-10T00:31:58.318Z\",\"firstName\":\"Allene\",\"lastName\":\"Hettinger\",\"email\":\"Conor_Haag31@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/gabrielrosser_128.jpg\",\"id\":\"14\"},{\"createdAt\":\"2021-09-10T07:49:11.617Z\",\"firstName\":\"Winona\",\"lastName\":\"Gusikowski\",\"email\":\"Leif60@hotmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/9lessons_128.jpg\",\"id\":\"15\"},{\"createdAt\":\"2021-09-10T06:22:50.046Z\",\"firstName\":\"Daryl\",\"lastName\":\"Rowe\",\"email\":\"Verda26@hotmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/sangdth_128.jpg\",\"id\":\"16\"},{\"createdAt\":\"2021-09-10T03:19:13.045Z\",\"firstName\":\"Frieda\",\"lastName\":\"Berge\",\"email\":\"Dorothea4@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/shalt0ni_128.jpg\",\"id\":\"17\"},{\"createdAt\":\"2021-09-10T04:19:10.240Z\",\"firstName\":\"Korbin\",\"lastName\":\"Schuppe\",\"email\":\"Litzy_Hoeger@hotmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/adellecharles_128.jpg\",\"id\":\"18\"},{\"createdAt\":\"2021-09-10T03:39:57.767Z\",\"firstName\":\"Alexys\",\"lastName\":\"Mayer\",\"email\":\"Wyatt59@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/guischmitt_128.jpg\",\"id\":\"19\"},{\"createdAt\":\"2021-09-10T07:12:37.707Z\",\"firstName\":\"Kacey\",\"lastName\":\"Bogisich\",\"email\":\"Nya69@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/unterdreht_128.jpg\",\"id\":\"20\"},{\"createdAt\":\"2021-09-09T21:39:55.839Z\",\"firstName\":\"Gerhard\",\"lastName\":\"Turcotte\",\"email\":\"Leone.Kovacek37@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/scott_riley_128.jpg\",\"id\":\"21\"},{\"createdAt\":\"2021-09-10T11:27:10.331Z\",\"firstName\":\"Jessica\",\"lastName\":\"Ferry\",\"email\":\"Bradly.Heller11@hotmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/marciotoledo_128.jpg\",\"id\":\"22\"},{\"createdAt\":\"2021-09-09T22:26:56.984Z\",\"firstName\":\"Bradford\",\"lastName\":\"Christiansen\",\"email\":\"Dion79@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/ainsleywagon_128.jpg\",\"id\":\"23\"},{\"createdAt\":\"2021-09-10T04:28:46.936Z\",\"firstName\":\"Marcellus\",\"lastName\":\"Cartwright\",\"email\":\"Edmond.Emmerich@yahoo.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/therealmarvin_128.jpg\",\"id\":\"24\"},{\"createdAt\":\"2021-09-09T19:32:17.520Z\",\"firstName\":\"Stanford\",\"lastName\":\"Kuhlman\",\"email\":\"Anthony.Lang64@gmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/josemarques_128.jpg\",\"id\":\"25\"},{\"createdAt\":\"2021-09-10T13:28:14.072Z\",\"firstName\":\"Adrain\",\"lastName\":\"Kutch\",\"email\":\"Giovani90@hotmail.com\",\"avatar\":\"https://cdn.fakercloud.com/avatars/marcusgorillius_128.jpg\",\"id\":\"26\"}]";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(input, Contact[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Contact[0];
        }
    }
}
