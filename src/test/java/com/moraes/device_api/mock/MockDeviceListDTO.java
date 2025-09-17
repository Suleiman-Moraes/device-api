package com.moraes.device_api.mock;

import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.mock.interfaces.AbstractMock;

public class MockDeviceListDTO extends AbstractMock<DeviceListDTO> {

    @Override
    protected Class<DeviceListDTO> getClazz() {
        return DeviceListDTO.class;
    }

    @Override
    protected void setOdersValues(DeviceListDTO dto, Integer number) {
        // No orders to set
    }
}
