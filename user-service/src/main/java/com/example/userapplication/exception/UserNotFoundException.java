package com.example.userapplication.exception;

public class UserNotFoundException extends RuntimeException {
    private String ecode;
    public UserNotFoundException(String message,String ecode) {
        super(message);
        this.ecode=ecode;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }
}
