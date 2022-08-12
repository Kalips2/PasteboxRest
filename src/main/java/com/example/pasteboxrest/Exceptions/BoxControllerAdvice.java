package com.example.pasteboxrest.Exceptions;

import com.example.pasteboxrest.PastModel.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.criteria.CriteriaBuilder;

@ControllerAdvice
public class BoxControllerAdvice{

    @ExceptionHandler(BoxNotExist.class)
    public ResponseEntity<?> handleBoxNotExist(BoxNotExist e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFindExpInSwitchException.class)
    public ResponseEntity<?> handleNotFindExpInSwitchException(NotFindExpInSwitchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(IncorrectHash.class)
    public ResponseEntity<?> handleIncorrectHash(IncorrectHash e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }
}
