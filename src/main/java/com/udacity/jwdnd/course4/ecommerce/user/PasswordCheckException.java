package com.udacity.jwdnd.course4.ecommerce.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "password validation failed")
public class PasswordCheckException extends RuntimeException {

    public PasswordCheckException(String message) {
        super(message);
    }
}
