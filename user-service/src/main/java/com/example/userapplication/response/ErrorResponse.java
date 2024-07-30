package com.example.userapplication.response;

public class ErrorResponse {
    private String message;
    private Throwable cause;
    private String ecode;
    private int status;

    public ErrorResponse(String message, Throwable cause, String ecode, int status) {
        this.message = message;
        this.cause=cause;
        this.ecode=ecode;
        this.status = status;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
