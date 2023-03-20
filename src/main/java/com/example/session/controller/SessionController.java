package com.example.session.controller;

import com.example.session.model.dto.MemberDto;
import com.example.session.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final UserServiceImpl userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/registerOk")
    public String registerOk(MemberDto memberDto) {
        userService.createAccount(memberDto);
        return "redirect:/login";
    }
}
