package dev.abhaya.mindstack.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "UTC")
    private Instant timeStamp;
    private String message;
    private int status;
    //private String path;

    public ApiError(){
        this.timeStamp = Instant.now();
    }

    public ApiError(String error, int statusCode){
        this();
        this.message = error;
        this.status = statusCode;

    }
}
