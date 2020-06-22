package com.lambda.watermyplants.models;

/**
 * A model to report a validation error
 */
public class ValidationError {

    private String Code;

    private String message;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidationError{" + "Code='" + Code + '\'' + ", message='" + message + '\'' + '}';
    }
}
