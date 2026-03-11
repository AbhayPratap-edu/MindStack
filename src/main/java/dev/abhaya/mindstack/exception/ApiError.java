package dev.abhaya.mindstack.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String message;
    private int status;
    //private String path;

    public ApiError(){
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String error, int statusCode){
        this();
        this.message = error;
        this.status = statusCode;

    }
}
