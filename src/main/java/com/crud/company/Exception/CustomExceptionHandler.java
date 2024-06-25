package com.crud.company.Exception;

import com.crud.company.enums.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handleMissingPathVariableException(MissingPathVariableException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now())
                .message(ex.getBody().getDetail()).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now())
                .message(Objects.requireNonNull(ex.getDetailMessageArguments())[1].toString()).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now()).message(ex.getMessage().split(":") [0]).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<?> handleBusinessValidationException(BusinessValidationException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException() {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(LocalDateTime.now())
                .message(ErrorMessage.AnotherRecordWithTheSameNameExist.getMessage()).build();
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
