package org.service.Models;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Person {
    private Long id;
    @Size(min = 1, max = 140,message = "Name should be real")
    private String fullName;
    @Min(value = 1, message = "You are too small")
    @Max(value = 150, message = "Can not live that long")
    private int age;

    private List<Book> books;

}
