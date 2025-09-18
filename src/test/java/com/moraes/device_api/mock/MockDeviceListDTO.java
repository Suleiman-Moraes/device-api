package com.moraes.device_api.mock;

import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;
import com.moraes.device_api.mock.interfaces.AbstractMock;

public class MockDeviceListDTO extends AbstractMock<DeviceListDTO> {

    public MockDeviceListDTO() {
        super();
        ignoreFields.add("DEVICE_LIST_DTO_MAPPING");
    }

    @Override
    protected Class<DeviceListDTO> getClazz() {
        return DeviceListDTO.class;
    }

    @Override
    protected void setOdersValues(DeviceListDTO dto, Integer number) {
        dto.setState(DeviceStateEnum.AVAILABLE);
    }
}
