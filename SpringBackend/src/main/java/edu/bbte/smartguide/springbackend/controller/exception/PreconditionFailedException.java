package edu.bbte.smartguide.springbackend.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
@Data
@AllArgsConstructor
public class PreconditionFailedException extends RuntimeException {
    private String message;



    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleBadRequest(PreconditionFailedException e) {
        return e.getMessage();
    }
}
