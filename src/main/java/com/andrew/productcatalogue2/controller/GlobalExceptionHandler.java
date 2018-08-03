package com.andrew.productcatalogue2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionSystemException.class)
    public String handleException(Model model, Exception ex) {
        log.info("in GlobalExceptionHandler handleException");
        String message = "Sorry, your request could not be completed due to a problem completing the transaction in the system";
        model.addAttribute("message", message);
        return "error/transaction-error";
    }


}
