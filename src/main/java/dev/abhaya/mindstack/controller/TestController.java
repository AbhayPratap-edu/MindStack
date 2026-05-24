package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.model.TestModel;
import dev.abhaya.mindstack.service.TestDBService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {

    private final TestDBService testDBService;

    public TestController(TestDBService testDBService) {
        this.testDBService = testDBService;
    }

    @GetMapping("/test/admin")
    public String adminOnly() {
        return "Admin access granted";
    }

    @GetMapping("/test/user")
    public String userOnly() {
        return "User access granted";
    }

    @GetMapping("/test/db")
    public List<TestModel> TestDB(){
        return testDBService.findAll() ;
    }

    @PostMapping("/test/db/add")
    public TestModel save(@RequestBody TestModel testModel) {
        return testDBService.saveTestUser(testModel);
    }

    @GetMapping("/test/db/user")
    public TestModel findByUser(@RequestParam String username) {
        return testDBService.getTestUser(username);
    }

}
