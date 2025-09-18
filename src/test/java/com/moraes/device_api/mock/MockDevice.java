package com.moraes.device_api.mock;

import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;
import com.moraes.device_api.mock.interfaces.AbstractMock;

public class MockDevice extends AbstractMock<Device> {

    @Override
    protected Class<Device> getClazz() {
        return Device.class;
    }

    @Override
    protected void setOdersValues(Device entity, Integer number) {
        entity.setState(DeviceStateEnum.AVAILABLE);
    }
}
