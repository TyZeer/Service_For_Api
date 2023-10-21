package org.service.Services;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.service.Models.Book;
import org.service.Models.Person;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final RestTemplate restTemplate;
    private final HttpClient httpClient = HttpClientBuilder.create().build();
    private final String BASE_URL = "http://localhost:8088/api/person";

    public List<Person> getAll() {
        ResponseEntity<List<Person>> exchange = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {
        });
        return exchange.getBody();
    }
    public Person getPerson(Long id){
        ResponseEntity<Person> exchange = restTemplate.exchange(BASE_URL+"/"+id.toString(),HttpMethod.GET,null,new ParameterizedTypeReference<Person>(){});
        return exchange.getBody();
    }

    public HttpStatus createPerson(Person person) {
        ResponseEntity<String> exchange = restTemplate.postForEntity(BASE_URL, person, String.class);
        return exchange.getStatusCode();
    }

    public HttpStatus editPerson(Person person) {
        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Person> requestEntity = new HttpEntity<>(person,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL+"/"+person.getId().toString(),HttpMethod.PUT,requestEntity,String.class);
        return responseEntity.getStatusCode();
    }

    public void delete(Long id) {
        restTemplate.delete(BASE_URL+"/"+id.toString());
    }
}
