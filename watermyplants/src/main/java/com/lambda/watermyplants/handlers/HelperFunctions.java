package com.lambda.watermyplants.handlers;

import com.lambda.watermyplants.exceptions.ResourceNotFoundException;
import com.lambda.watermyplants.models.ValidationError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class contains helper functions - functions that are needed throughout the application. The class can be autowired
 * into any class.
 */
@Component
public class HelperFunctions {

    public boolean isAuthorizedToMakeChange(String username) {
        // Check to see if the user whose information being requested is the current user
        // Check to see if the requesting user is an admin
        // if either is true, return true
        // otherwise stop the process and throw an exception
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (username.equalsIgnoreCase(authentication.getName()
                .toLowerCase()) || authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // this user can make this change
            return true;
        } else {
            // stop user is not authorized to make this change so stop the whole process and throw an exception
            throw new ResourceNotFoundException(authentication.getName() + " not authorized to make change");
        }
    }

    public List<ValidationError> getConstraintViolation(Throwable cause) {
        // Find any data violations that might be associated with the error and report them
        // data validations get wrapped in other exceptions as we work through the Spring
        // exception chain. Hence we have to search the entire Spring Exception Stack
        // to see if we have any violation constraints.
        while ((cause != null) && !(cause instanceof ConstraintViolationException)) {
            cause = cause.getCause();
        }

        List<ValidationError> validationErrorList = new ArrayList<>();

        // we know that cause either null or an instance of ConstraintViolationException
        if (cause != null) {
            ConstraintViolationException ex = (ConstraintViolationException) cause;
            for (ConstraintViolation cv : ex.getConstraintViolations()) {
                ValidationError newVe = new ValidationError();
                newVe.setCode(cv.getInvalidValue()
                        .toString());
                newVe.setMessage(cv.getMessage());
                validationErrorList.add(newVe);
            }
        }
        return validationErrorList;
    }
}
