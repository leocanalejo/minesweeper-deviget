package com.lcanalejo.deviget.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class EntityDuplicatedException extends RuntimeException {

    public EntityDuplicatedException(String message) {
        super(message);
    }

}
