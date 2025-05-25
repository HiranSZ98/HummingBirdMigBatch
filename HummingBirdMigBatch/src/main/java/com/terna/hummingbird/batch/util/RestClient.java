package com.terna.hummingbird.batch.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.terna.hummingbird.batch.model.DocumentSentPayload;
import com.terna.hummingbird.batch.model.ResponseCreateDoc;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestClient {

    private static RestTemplate restTemplate = new RestTemplate();

    public static ResponseCreateDoc callCreateDocument(String payload, String url) throws Exception {
        ResponseCreateDoc responseCreate = null;
        String username = "USRMIGR";
        String password = "uSrTKY7!?_cqW";
        String base64Creds = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + base64Creds);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        ResponseEntity<ResponseCreateDoc> response = restTemplate.postForEntity(url, request, ResponseCreateDoc.class);
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode != HttpStatus.OK && statusCode != HttpStatus.CREATED) {
           throw new Exception("Error calling rest...");
        } else {
            responseCreate = response.getBody();
        }
        return responseCreate;
    }

}
