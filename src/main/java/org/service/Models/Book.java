package org.service.Models;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Book {
    public Book(Long id, @NonNull String title, @NonNull String author, int writeYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.writeYear = writeYear;
        this.person_id = null;
    }
    public Book(BookResponse response){
        this.id = response.getBook().getId();
        this.title = response.getBook().getTitle();
        this.author = response.getBook().getAuthor();
        this.writeYear =response.getBook().getWriteYear();
        this.person_id= (long) response.getPerson_id();
    }
    @SerializedName("id")
    private Long id;
    @NonNull
    @Size(min = 1, max = 140,message = "Title should be real")
    @SerializedName("title")
    private String title;
    @NonNull
    @Size(min = 1, max = 140,message = "Author should be real")
    @SerializedName("author")
    private String author;
    @Min(value = 1,message = "Cant be less than 1")
    @Max(value = 2023,message = "Cant be in future")
    @SerializedName("writeYear")
    private int writeYear;
    @SerializedName("person_id")
    private Long person_id;
}
