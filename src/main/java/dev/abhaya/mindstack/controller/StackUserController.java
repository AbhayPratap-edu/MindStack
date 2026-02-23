//package dev.abhaya.mindstack.controller;
//
//import dev.abhaya.mindstack.dto.stackuser.RegisterStackUserRequest;
//import dev.abhaya.mindstack.dto.stackuser.RegisterStackUserResponse;
//import dev.abhaya.mindstack.service.StackUserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/Stackusers")
//@RequiredArgsConstructor
//public class StackUserController {
//
//    private final StackUserService stackUserService;
//
//
//    @PostMapping()
//    public ResponseEntity<RegisterStackUserResponse> registerStackUser(
//            @RequestBody RegisterStackUserRequest registerStackUserRequest){
//        return ResponseEntity.ok(stackUserService.RegisterStackUser(registerStackUserRequest));
//
//    }
//
//    @GetMapping("/{userid}")
//    public ResponseEntity<Void> signIn(@PathVariable Long userid){
//        stackUserService.SignInUser(userid);
//        return ResponseEntity.noContent().build();
//    }
//}
