package dev.tolstov.buspark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {


    @ResponseBody()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    String constraintViolationExceptionHandler(ConstraintViolationException exception) {
        return exception.getMessage();
    }


//    Используется вместе с @Valid. Пока не нужен
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult()
//                .getAllErrors()
//                .forEach((error) -> {
//                    String fieldName = ((FieldError) error).getField();
//                    String errorMessage = error.getDefaultMessage();
//                    errors.put(fieldName, errorMessage);
//                });
//        return errors;
//    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BPEntityNotFoundException.class)
    String bpEntityNotFoundException(BPEntityNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    String entityExistsException(EntityExistsException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmployeeException.class)
    String employeeException(EmployeeException exception) {
        return exception.getMessage();
    }
}
