package com.example.openwxchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("message", "Hello from Thymeleaf!");
        return "index";
    }
}
