package com.moraes.device_api.mock;

import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.mock.interfaces.AbstractMock;

public class MockDeviceDTO extends AbstractMock<DeviceDTO> {

    @Override
    protected Class<DeviceDTO> getClazz() {
        return DeviceDTO.class;
    }

    @Override
    protected void setOdersValues(DeviceDTO dto, Integer number) {
        // No orders to set
    }
}
