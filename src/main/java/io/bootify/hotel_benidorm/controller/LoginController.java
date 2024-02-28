package io.bootify.hotel_benidorm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Nombre del archivo de la vista de login (por ejemplo, login.html en src/main/resources/templates)
    }
}