package com.moraes.device_api.api.util;

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.moraes.device_api.api.exception.ValidException;
import com.moraes.device_api.api.model.dto.ExceptionUtilDTO;

public final class ExceptionsUtil {

    private ExceptionsUtil() {
    }

    /**
     * if condition == false
     * throw ValidException
     * 
     * @param condition
     * @param messageKey
     */
    public static void throwValidException(boolean condition, String message) {
        if (!condition) {
            throw new ValidException(message);
        }
    }

    /**
     * Throw a ValidException with the given exceptions if they are not null
     * and if any of the exceptions has isNotCondition() == true.
     * The exception will contain all the messages of the exceptions that have
     * isNotCondition() == true.
     * 
     * @param exceptions the exceptions to be checked
     */
    public static void throwValidExceptions(ExceptionUtilDTO... exceptions) {
        if (exceptions != null) {
            List<String> errors = new LinkedList<>();
            for (ExceptionUtilDTO exception : exceptions) {
                if (exception.isNotCondition()) {
                    errors.add(exception.getMessage());
                }
            }
            if (!CollectionUtils.isEmpty(errors)) {
                throw new ValidException(errors);
            }
        }
    }
}
