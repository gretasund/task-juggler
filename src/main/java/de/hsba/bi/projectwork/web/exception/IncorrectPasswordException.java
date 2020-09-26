package de.hsba.bi.projectwork.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}