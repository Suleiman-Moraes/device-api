package com.moraes.device_api.api.model.dto.device;

import org.springframework.data.domain.Sort.Direction;

import com.moraes.device_api.api.model.dto.interfaces.IFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeviceFilterDTO implements IFilterDTO {

    @Builder.Default
    private boolean paginate = false;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    private Direction direction;

    @Builder.Default
    private String property = "id";

    private String searchText;

    private String name;

    private String brand;
}
