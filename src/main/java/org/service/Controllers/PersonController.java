package org.service.Controllers;
import org.service.Models.Person;
import org.service.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {
    @Autowired
    PersonService personService;

    @GetMapping()
    public String allPeople(Model model){
        model.addAttribute("people",personService.getAll());
        return "people/all";
    }
    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") Long id,Model model){
        model.addAttribute("person",personService.getPerson(id));
        return "people/show";
    }
    @PostMapping("")
    public String createBook(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){ //Работает не так, надо надо понять как делать
        if(bindingResult.hasErrors()){
            return "/people/new";
        }
        HttpStatus answer =  personService.createPerson(person);
        if(answer == HttpStatus.CREATED){
            return "redirect:/people";
        }
        else
            return "redirect:/people/new";
    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person")Person person){

        return "/people/new";
    }
    @PutMapping("/{id}")
    public String editPerson( @PathVariable("id")Long id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        if (!bindingResult.hasErrors()) {
            if (personService.editPerson(person) == HttpStatus.CREATED)
                return "redirect:/people";
        }
        return "people/edit";
    }
    @GetMapping("/{id}/edit")
    public String getEdit(@PathVariable("id") Long id, Model model ) throws IOException {
        model.addAttribute("person", personService.getPerson(id));
        return "people/edit";
    }
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        personService.delete(id); //не тестировано не будет работать с книгами которые уже даны пользователи
        return "redirect:/people";
    }

}
