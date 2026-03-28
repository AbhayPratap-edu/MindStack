package dev.abhaya.mindstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
@Async
public class MindStackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindStackApplication.class, args);
    }

}
