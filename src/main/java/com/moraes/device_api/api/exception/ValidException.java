package com.moraes.device_api.api.exception;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.CONFLICT)
public class ValidException extends PatternException {

    @Getter
    private List<String> errs = new LinkedList<>();

    public ValidException(String message) {
        this(message, HttpStatus.CONFLICT);
    }

    public ValidException(String... messages) {
        super(Arrays.toString(messages), HttpStatus.CONFLICT);
        errs = Arrays.asList(messages);
    }

    public ValidException(List<String> messages) {
        super(messages.toString(), HttpStatus.CONFLICT);
        errs = messages;
    }

    public ValidException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
        errs.add(message);
    }
}
