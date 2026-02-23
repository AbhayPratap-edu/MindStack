package dev.abhaya.mindstack;

import dev.abhaya.mindstack.Security.JWTService;
import dev.abhaya.mindstack.model.StackUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Stack;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@SpringBootTest
class JWTApplicationTests {

    @Autowired
    private JWTService jwtService;
    @Test

    void contextLoads() {
        //StackUser user = new StackUser(<user id>,<user email>,<user password>);
       // String token = jwtService.createToken(user);
        //System.out.println("Create Token: "+token);
        //Long id = jwtService.verifyTokenAndGetUserId(token);   //to varify the token you can directly use that token here
        //System.out.println("Generate Id from Token: "+id);


    }

}
