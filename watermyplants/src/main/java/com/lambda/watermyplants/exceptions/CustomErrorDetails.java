package com.lambda.watermyplants.exceptions;

import com.lambda.watermyplants.handlers.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to override the error details provided by Spring Boot. We want to use our own format.
 */
@Component
public class CustomErrorDetails extends DefaultErrorAttributes {

    @Autowired
    private HelperFunctions helper;

    @Override
    public Map<String, Object> getErrorAttributes(
            WebRequest webRequest,
            boolean includeStackTrace) {

        //Get all the normal error information
        Map<String, Object> errorAttributes =
                super.getErrorAttributes(webRequest,
                        includeStackTrace);

        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("title",
                errorAttributes.get("error"));
        errorDetails.put("status",
                errorAttributes.get("status"));
        errorDetails.put("detail",
                errorAttributes.get("message"));
        errorDetails.put("timestamp",
                errorAttributes.get("timestamp"));
        errorDetails.put("developerMessage",
                "path: " + errorAttributes.get("path"));

        errorDetails.put("errors",
                helper.getConstraintViolation(this.getError(webRequest)));
        return errorDetails;
    }
}
