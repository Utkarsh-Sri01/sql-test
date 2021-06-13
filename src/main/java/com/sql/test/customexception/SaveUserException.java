package com.sql.test.customexception;

public class SaveUserException extends RuntimeException {
    public SaveUserException(String message) {
        super(message);
    }
}
