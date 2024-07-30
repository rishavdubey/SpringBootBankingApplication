package com.example.accountservice.exception;

public class AccountClosingException extends GlobalException{

    public AccountClosingException(String errorMessage) {
        super(GlobalErrorCode.BAD_REQUEST, errorMessage);
    }
}
