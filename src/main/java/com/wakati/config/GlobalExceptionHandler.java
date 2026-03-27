package com.wakati.config;

import com.wakati.exception.WakatiException;
import com.wakati.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserContext userContext;

    @ExceptionHandler(WakatiException.class)
    public ResponseEntity<?> handleAppException(WakatiException ex) {

        String lang = userContext.getCurrentLanguage();

        String message = messageService.getMessage(
                ex.getMessageKey(),
                lang
        );

        return ResponseEntity.badRequest().body(
                Map.of(
                        "code", ex.getStatus(),
                        "message", message,
                        "key", ex.getMessageKey()
                )
        );
    }
}