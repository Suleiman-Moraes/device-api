package com.moraes.device_api.api.model.dto.device;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceListDTO implements Serializable {

    private Long id;

    private String name;

    private String brand;

    private DeviceStateEnum state;

    private LocalDateTime creationTime;
}
