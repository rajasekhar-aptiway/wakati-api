package com.wakati.model.response;

import com.wakati.I18NConstants;
import com.wakati.constant.AppConstants;
import com.wakati.service.MessageService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResponseBuilder {

    @Autowired
    private MessageService messageService;

    public Map<String, Object> success(I18NConstants key, Object data) {
        return Map.of(
                "code", 200,
                "traceId",MDC.get(AppConstants.TRACE_ID),
                "message", messageService.get(key),
                "response", data
        );
    }

    public Map<String, Object> success(I18NConstants key, String responseKey, Object data) {
        return Map.of(
                "code", 200,
                "traceId",MDC.get(AppConstants.TRACE_ID) == null ? "" : MDC.get(AppConstants.TRACE_ID),
                "message", messageService.get(key,responseKey),
                responseKey, data
        );
    }

    public Map<String, Object> error(HttpStatus status, I18NConstants key) {
        return Map.of(
                "code", status.value(),
                "traceId",MDC.get(AppConstants.TRACE_ID),
                "message", messageService.get(key)
        );
    }
}