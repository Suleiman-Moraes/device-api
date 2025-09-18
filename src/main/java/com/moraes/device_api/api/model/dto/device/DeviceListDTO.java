package com.moraes.device_api.api.model.dto.device;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class DeviceListDTO implements Serializable {

    public static final String DEVICE_LIST_DTO_MAPPING = "DeviceListDTOMapping";

    private Long id;

    private String name;

    private String brand;

    private DeviceStateEnum state;

    private LocalDateTime creationTime;

    // DeviceListDTOMapping
    public DeviceListDTO(Long id, String name, String brand, String state, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = DeviceStateEnum.valueOf(state);
        this.creationTime = creationTime;
    }
}
