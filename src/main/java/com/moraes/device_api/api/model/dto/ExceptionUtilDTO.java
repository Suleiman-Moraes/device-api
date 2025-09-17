package com.moraes.device_api.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionUtilDTO {

    private boolean condition;
    private String message;

    public boolean isNotCondition() {
        return !condition;
    }
}
