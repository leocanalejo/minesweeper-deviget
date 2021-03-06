package com.lcanalejo.deviget.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.PRECONDITION_FAILED)
public class PreconditionException extends RuntimeException {

    public PreconditionException(String message) {
        super(message);
    }

}
