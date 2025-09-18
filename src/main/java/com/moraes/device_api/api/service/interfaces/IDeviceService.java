package com.moraes.device_api.api.service.interfaces;

import com.moraes.device_api.api.exception.ResourceNotFoundException;
import com.moraes.device_api.api.exception.ValidException;
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

    /**
     * Updates a device with the given ID using the given DeviceDTO object.
     * <p>
     * This method is transactional and will rollback if an exception occurs.
     * <p>
     * The device will be fetched from the database using the given ID.
     * If the device is not found, a ResourceNotFoundException will be thrown.
     * <p>
     * The method will then update the fetched device with the values from the given
     * DeviceDTO object.
     * If the device is in use, a ValidException will be thrown with the conditions
     * that cannot be changed.
     * <p>
     * The method will then save the updated device to the database.
     * <p>
     * A debug message will be logged with the ID of the updated device.
     * 
     * @param id  the ID of the device to update
     * @param dto the DeviceDTO object containing the device data to update
     * @throws ResourceNotFoundException if the device is not found
     * @throws ValidException            if the device is in use and the conditions
     *                                   cannot be changed
     */
    void update(Long id, DeviceDTO dto);

    /**
     * Deletes a device with the given ID.
     * <p>
     * This method is transactional and will rollback if an exception occurs.
     * <p>
     * The device will be fetched from the database using the given ID.
     * If the device is not found, a ResourceNotFoundException will be thrown.
     * <p>
     * If the device is in use, a ValidException will be thrown with the condition
     * that cannot be changed.
     * <p>
     * The method will then delete the device from the database.
     * <p>
     * A debug message will be logged with the ID of the deleted device.
     * 
     * @param id the ID of the device to delete
     * @throws ResourceNotFoundException if the device is not found
     * @throws ValidException            if the device is in use and the conditions
     *                                   cannot be changed
     */
    void delete(Long id);
}
