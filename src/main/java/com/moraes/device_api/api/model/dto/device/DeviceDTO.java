package com.moraes.device_api.api.model.dto.device;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.device_api.api.controller.interfaces.PartialChecks;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO implements Serializable {

    @NotBlank
    @Size(min = 1, max = 150)
    @Size(max = 150, groups = { PartialChecks.class })
    private String name;

    @NotBlank
    @Size(min = 5, max = 150)
    @Size(max = 150, groups = { PartialChecks.class })
    private String brand;

    @NotNull
    private DeviceStateEnum state;
}
