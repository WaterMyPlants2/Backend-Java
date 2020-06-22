package com.lambda.watermyplants.handlers;

import com.lambda.watermyplants.exceptions.ResourceFoundException;
import com.lambda.watermyplants.exceptions.ResourceNotFoundException;
import com.lambda.watermyplants.models.ErrorDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * This is the driving class when an exception occurs. All exceptions are handled here.
 * This class is shared across all controllers due to the annotation RestControllerAdvice;
 * this class gives advice to all controllers on how to handle exceptions.
 * Due to the annotation Order(Ordered.HIGHEST_PRECEDENCE), this class takes precedence over all other controller advisors.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private HelperFunctions helper;

    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("Resource Not Found");
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setDeveloperMessage(rnfe.getClass()
                .getName());
        errorDetail.setErrors(helper.getConstraintViolation(rnfe));

        return new ResponseEntity<>(errorDetail,
                null,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<?> handleResourceFoundException(ResourceFoundException rfe) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Unexpected Resource");
        errorDetail.setDetail(rfe.getMessage());
        errorDetail.setDeveloperMessage(rfe.getClass()
                .getName());
        errorDetail.setErrors(helper.getConstraintViolation(rfe));

        return new ResponseEntity<>(errorDetail,
                null,
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Rest Internal Exception");
        errorDetail.setDetail(ex.getMessage());
        errorDetail.setDeveloperMessage(ex.getClass()
                .getName());
        errorDetail.setErrors(helper.getConstraintViolation(ex));

        return new ResponseEntity<>(errorDetail,
                null,
                status);
    }

}
