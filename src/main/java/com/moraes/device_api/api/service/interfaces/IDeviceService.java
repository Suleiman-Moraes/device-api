package com.moraes.device_api.api.service.interfaces;

import com.moraes.device_api.api.model.dto.device.DeviceDTO;

public interface IDeviceService {

    /**
     * Inserts a new device into the database.
     * <p>
     * This method is transactional and will rollback if an exception occurs.
     * <p>
     * The inserted device will be parsed from the given DeviceDTO object.
     * <p>
     * The ID of the inserted device will be returned.
     * 
     * @param dto the DeviceDTO object containing the device data to be inserted.
     * @return the ID of the inserted device.
     */
    Long insert(DeviceDTO dto);
}
