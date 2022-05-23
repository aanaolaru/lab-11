package com.example.client;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class CallService {

    final Logger log = (Logger) LoggerFactory.getLogger(CallService.class);
    final String uri = "http://localhost:8081/people";

    @GetMapping("/call")
    public String getProducts() {
        log.info("Start");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        String result = response.getBody();
        log.info(result);
        log.info("Stop");
        return result;
    }

}
