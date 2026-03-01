package dev.abhaya.mindstack.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin/test")
    public String adminOnly() {
        return "Admin access granted";
    }

    @GetMapping("/user/test")
    public String userOnly() {
        return "User access granted";
    }
}
