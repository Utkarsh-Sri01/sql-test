package com.sql.test.customexception;

public class QueryExecutionException extends RuntimeException {
    public QueryExecutionException(String exception) {
        super(exception);
    }
}
