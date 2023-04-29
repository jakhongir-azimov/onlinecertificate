package com.company.certificate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/users")
    public String user(){
        return "users";
    }
    @GetMapping("/directions")
    public String directions(){
        return "directions";
    }
    @GetMapping("/certificates")
    public String certificates(){
        return "certificates";
    }
    @GetMapping("/certificates/**")
    public String certificate(Model model){
        return "certificate";
    }
}
