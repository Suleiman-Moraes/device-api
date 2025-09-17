package com.moraes.device_api.api.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.moraes.device_api.api.exception.ValidException;
import com.moraes.device_api.api.model.dto.ExceptionUtilDTO;

class ExceptionsUtilTest {

    @Test
    void testThrowValidException() {
        assertThrows(ValidException.class, () -> ExceptionsUtil.throwValidException(false, "Error Message"),
                "Does Not Throw");
    }

    @Test
    void testThrowValidExceptions() {
        final ExceptionUtilDTO dto = ExceptionUtilDTO.builder().condition(false).message("Error Message")
                .build();
        assertThrows(ValidException.class,
                () -> ExceptionsUtil.throwValidExceptions(
                        dto),
                "Does Not Throw");
    }
}