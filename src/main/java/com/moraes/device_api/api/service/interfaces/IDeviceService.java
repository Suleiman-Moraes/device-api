package com.moraes.device_api.api.service.interfaces;

import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;

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

    /*
     * Retrieves a device DTO by its ID.
     * <p>
     * This method is transactional and will rollback if an exception occurs.
     * <p>
     * The method is read-only and will not modify the database.
     * <p>
     * The method will log a debug message with the given ID and the fetched device.
     * <p>
     * The method will return the device DTO mapped from the fetched device.
     * 
     * @param id the ID of the device to fetch
     * 
     * @return the device DTO mapped from the fetched device
     */
    DeviceListDTO getDTOById(Long id);
}
