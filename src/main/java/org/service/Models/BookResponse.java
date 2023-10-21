package org.service.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BookResponse {
    private Book book;
    private int person_id;

    public void setBook(Book book) {
        this.book = book;
    }

    public int getPersonId() {
        return person_id;
    }

    public void setPersonId(int person_id) {
        this.person_id = person_id;
    }
}
