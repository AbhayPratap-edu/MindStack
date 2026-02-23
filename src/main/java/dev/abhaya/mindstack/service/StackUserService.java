//package dev.abhaya.mindstack.service;
//
//import dev.abhaya.mindstack.dto.stackuser.RegisterStackUserRequest;
//import dev.abhaya.mindstack.dto.stackuser.RegisterStackUserResponse;
//import dev.abhaya.mindstack.model.StackUser;
//import dev.abhaya.mindstack.repository.StackUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class StackUserService {
//
//    private final StackUserRepository stackUserRepository;
//
//
//    public RegisterStackUserResponse RegisterStackUser(RegisterStackUserRequest registerStackUserRequest) {
//        StackUser stackUser = new StackUser();
//        stackUser.setEmail(registerStackUserRequest.getEmail());
//        stackUser.setPassword(registerStackUserRequest.getPassword());
//        stackUserRepository.save(stackUser);
//        return new RegisterStackUserResponse(stackUser.getUserID());
//    }
//
//
//}
