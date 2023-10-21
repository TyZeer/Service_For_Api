package org.service.Services;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.service.Models.Book;
import org.service.Models.BookResponse;
import org.service.Models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private PersonService personService;

    private final RestTemplate restTemplate;
    private final HttpClient httpClient = HttpClientBuilder.create().build();
    private final String BASE_URL = "http://localhost:8088/api/books";

    public List<Book> getAllBooks() {
        ResponseEntity<List<Book>> exchange =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
        });
        return exchange.getBody();
    }

    public Optional<Book> getBook(Long person_id) throws IOException { //Мб попробовать тоже restTemplate ыгыгыгы
        HttpGet httpGet = new HttpGet(BASE_URL + "/" + person_id.toString());
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
           Book book = new Book(objectMapper.readValue(responseBody, BookResponse.class));
           return Optional.of(book);


        } catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.ofNullable(objectMapper.readValue(responseBody, Book.class));

        }

    }

    public HttpStatus createBook(Book book) {
        ResponseEntity<String> exchange = restTemplate.postForEntity(BASE_URL, book, String.class);
        return exchange.getStatusCode();
    }

    public HttpStatus editBook(Book book) {
        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Book> requestEntity = new HttpEntity<>(book,headers);
       ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL+"/"+book.getId().toString(),HttpMethod.PUT,requestEntity,String.class);
        return responseEntity.getStatusCode();
    }

    public Optional<Person> getOwner(Long id) throws IOException {
        Optional<Book> book = getBook(id);

        return book.map(value -> personService.getPerson(value.getPerson_id()));

    }

    public void delete(Long id) {
        restTemplate.delete(BASE_URL+"/"+id.toString());
    }

    public void release(Long id) {
        restTemplate.put(BASE_URL+"/"+id.toString()+"/release",HttpMethod.PUT);
    }
    public void assign(Long id, Long person_id){
        restTemplate.put(BASE_URL+"/"+id.toString()+"/assign?person="+person_id.toString(),HttpMethod.PUT);
    }
}


