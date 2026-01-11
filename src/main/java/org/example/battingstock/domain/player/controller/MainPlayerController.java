package org.example.battingstock.domain.player.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPlayerController {
    @GetMapping("/")
    public String mainPage(Model model) {
        return "main";
    }
}
