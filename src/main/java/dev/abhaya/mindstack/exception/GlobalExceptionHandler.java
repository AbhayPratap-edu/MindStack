package dev.abhaya.mindstack.exception;

import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import dev.abhaya.mindstack.exception.customException.UserAlreadyExistsException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //CustomMessageException
    @ExceptionHandler(CustomMessageException.class)
    public ResponseEntity<ApiError> handleCustomMessageException(CustomMessageException e) {
        ApiError apiError = new ApiError(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    //UserAlreadyExitsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCustomException(UserAlreadyExistsException userAlreadyExistsException) {
        ApiError apiError = new ApiError(userAlreadyExistsException.getLocalizedMessage(),
                HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    //AuthorizationException
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        ApiError apiError = new ApiError(accessDeniedException.getLocalizedMessage(),
                HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    //AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException authenticationException){
        ApiError apiError = new ApiError(authenticationException.getLocalizedMessage(),HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    //JWTException
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException jwtException){
        ApiError apiError = new ApiError(jwtException.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    //@ExceptionHandler(ControllerThrowException.class)

}
