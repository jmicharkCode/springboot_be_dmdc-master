package bkav.com.springboot.controllers;

import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.Message;
import bkav.com.springboot.payload.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<?> conflictData(Exception e) {
        logger.info(e.getMessage());
        return ResponseEntity.ok(new MessageResponse(Status.METHOD_NOT_ALLOWED, Message.METHOD_NOT_ALLOWED, false, true));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> badRequestHandle(Exception e) {
        logger.info(e.getMessage());
        return ResponseEntity.ok(new MessageResponse(Status.BAD_REQUEST, Message.BAD_REQUEST, false, true));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> internalServerError(Exception e) {
        logger.info(e.getMessage());
        return ResponseEntity.ok(new MessageResponse(Status.INTERNAL_SERVER_ERROR, Message.INTERNAL_SERVER_ERROR, false, true));
    }
}
