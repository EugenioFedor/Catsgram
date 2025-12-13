package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/home")
    public String homePage() {
        return "<h1>Дороу вас в приложении Котограм<h1>";
    }
}