package de.hsba.bi.projectwork.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String msg) {
        super(msg);
    }

}