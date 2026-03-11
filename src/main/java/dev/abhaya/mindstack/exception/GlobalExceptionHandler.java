package dev.abhaya.mindstack.exception;

import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import dev.abhaya.mindstack.exception.customException.UserAlreadyExistsException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //ValidationEXception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((FieldError error) ->
                        error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ApiError apiError = new ApiError(message, HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    //CustomMessageException
    @ExceptionHandler(CustomMessageException.class)
    public ResponseEntity<ApiError> handleCustomMessageException(CustomMessageException e) {
        ApiError apiError = new ApiError(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.getStatus()));
    }

    //UserAlreadyExitsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCustomException(UserAlreadyExistsException userAlreadyExistsException) {
        ApiError apiError = new ApiError(userAlreadyExistsException.getLocalizedMessage(),
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    //AuthorizationException
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        ApiError apiError = new ApiError(accessDeniedException.getLocalizedMessage(),
                HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    //AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException authenticationException){
        ApiError apiError = new ApiError(authenticationException.getLocalizedMessage(),HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    //JWTException
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException jwtException){
        ApiError apiError = new ApiError(jwtException.getLocalizedMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    //@ExceptionHandler(ControllerThrowException.class)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex){

        ApiError apiError = new ApiError(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
