package com.example.BlogAppApi.exceptions;

public class DuplicateUserException extends Exception {
    private String message;
    public DuplicateUserException(String message){
        super(message);
        this.message = message;
    }


}
