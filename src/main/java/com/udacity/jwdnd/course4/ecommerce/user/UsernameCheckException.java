package com.udacity.jwdnd.course4.ecommerce.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "username validation failed")
public class UsernameCheckException extends RuntimeException {

    public UsernameCheckException(String message) {
        super(message);
    }
}
