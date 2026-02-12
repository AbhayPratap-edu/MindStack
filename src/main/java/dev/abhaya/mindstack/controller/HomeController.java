package dev.abhaya.mindstack.controller;


import dev.abhaya.mindstack.service.NoteBookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    public HomeController() {
        System.out.println("Hello World");
    }

    NoteBookService noteBookService ;

    @GetMapping
    public String HomePage() {
        return "Hello World";
    }





}
