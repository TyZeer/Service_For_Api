package org.service.Controllers;

import org.service.Models.Book;
import org.service.Models.BookResponse;
import org.service.Models.Person;
import org.service.Services.BookService;
import org.service.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private PersonService personService;
    @GetMapping("")
    public String allBooks(Model model) {
        model.addAttribute("books",bookService.getAllBooks());
        return "books/all";
    }
    @GetMapping("/{id}")
    public String getBook( Model model, @ModelAttribute("person") Person person, @PathVariable("id") Long id) throws IOException {
        Optional<Book> book = bookService.getBook(id);
        try {
            model.addAttribute("book", book);
            if (book.get().getPerson_id() != null) {
                model.addAttribute("owner", personService.getPerson(book.get().getPerson_id()));
            } else {
                model.addAttribute("people", personService.getAll());
            }
            return "books/show";
        }catch (Exception e){
            return "redirect:books/all";
        }


    }
    @PostMapping("")
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){ //Работает не так, надо надо понять как делать
       if(bindingResult.hasErrors()){
           return "/books/new";
       }
        HttpStatus answer =  bookService.createBook(book);
       if(answer == HttpStatus.CREATED){
           return "redirect:/books";
       }
       else
           return "redirect:/books/new";
    }
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book){
        return "/books/new";
    }

    @PutMapping("/{id}")
    public String editBook( @PathVariable("id")Long id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (!bindingResult.hasErrors())
            if (bookService.editBook(book) == HttpStatus.CREATED)
                return "redirect:/books";
        return "books/edit";
    }
    @GetMapping("/{id}/edit")
    public String getEdit(@PathVariable("id") Long id, Model model ) throws IOException {
        model.addAttribute("book", bookService.getBook(id).get());
        return "books/edit";
    }
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookService.delete(id); //не тестировано не будет работать с книгами которые уже даны пользователи
        return "redirect:/books";
    }
    @PutMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") Long id){
        bookService.release(id);
        return "redirect:/books/{id}"; //пока может не работать
    }
    @PutMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") Long id, @ModelAttribute("person") Person selectedPerson){
        bookService.assign(id, selectedPerson.getId());
        return "redirect:/books/{id}";
    }


}
