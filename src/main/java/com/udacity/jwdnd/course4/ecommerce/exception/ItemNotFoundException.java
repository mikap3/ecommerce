package com.udacity.jwdnd.course4.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item not found")
public class ItemNotFoundException extends RuntimeException{

}
