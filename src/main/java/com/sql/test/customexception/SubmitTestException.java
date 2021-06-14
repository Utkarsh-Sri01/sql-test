package com.sql.test.customexception;

public class SubmitTestException extends RuntimeException {
    public SubmitTestException(String exception) {
        super(exception);
    }
}
