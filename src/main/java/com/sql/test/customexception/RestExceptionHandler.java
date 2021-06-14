package com.sql.test.customexception;

import com.sql.test.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String SUBMIT_ERROR_MSG = "Something went wrong !! Please contact Alooba Helpdesk";
    private static final String START_TEST_ERROR_MSG = "Cannot start the test, Please contact Alooba Helpdesk";

    @ExceptionHandler(QueryExecutionException.class)
    protected ResponseEntity<ApiError> handleQueryExecutionException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SubmitTestException.class)
    protected ResponseEntity<ApiError> handleSubmitTestException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, SUBMIT_ERROR_MSG);
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SaveUserException.class)
    protected ResponseEntity<ApiError> handleSaveUserException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, START_TEST_ERROR_MSG);
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
