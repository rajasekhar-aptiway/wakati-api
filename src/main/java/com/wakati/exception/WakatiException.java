package com.wakati.exception;

import com.wakati.I18NConstants;
import org.springframework.http.HttpStatus;

public class WakatiException
        extends RuntimeException {

    private final HttpStatus status;
    private final String messageKey;
    private final Object[] args;

    public WakatiException(I18NConstants messageKey, Object... args) {
        this.status = HttpStatus.BAD_REQUEST;
        this.messageKey = messageKey.name();
        this.args = args;
    }

    public WakatiException(HttpStatus status,I18NConstants messageKey, Object... args) {
        this.status = status;
        this.messageKey = messageKey.name();
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public HttpStatus getStatus(){
        return status;
    }
}