package com.moraes.device_api.api.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.device_api.api.exception.ResourceNotFoundException;
import com.moraes.device_api.api.exception.ValidException;
import com.moraes.device_api.api.mapper.IDeviceMapper;
import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.ExceptionUtilDTO;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceFilterDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;
import com.moraes.device_api.api.repository.IDeviceCustomRepository;
import com.moraes.device_api.api.repository.IDeviceRepository;
import com.moraes.device_api.api.service.interfaces.IDeviceService;
import com.moraes.device_api.api.util.ExceptionsUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceService implements IDeviceService {

    private final IDeviceRepository repository;
    private final IDeviceCustomRepository customRepository;

    private final IDeviceMapper mapper;

    @Transactional
    @Override
    public Long insert(DeviceDTO dto) {
        log.debug("Inserting a new device: {}", dto);
        Device object = mapper.toEntity(dto);
        object = repository.save(object);
        log.debug("Device inserted with ID: {}", object.getId());
        return object.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public DeviceListDTO getDTOById(Long id) {
        log.debug("Fetching device DTO with ID: {}", id);
        final Device object = getById(id);
        log.debug("Device fetched: {}", object);
        return mapper.toListDTO(object);
    }

    @Transactional
    @Override
    public void update(Long id, DeviceDTO dto) {
        log.debug("Updating device with ID: {} using data: {}", id, dto);
        Device existingObject = getById(id);
        mapper.updateFromDeviceDTO(dto, existingObject);
        validateBeforeUpdate(existingObject, dto);
        repository.save(existingObject);
        log.debug("Device with ID: {} updated successfully", id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Deleting device with ID: {}", id);
        final Device object = getById(id);
        ExceptionsUtil.throwValidExceptions(
                ExceptionUtilDTO.builder()
                        .condition(!DeviceStateEnum.IN_USE.equals(object.getState()))
                        .message("Device in use cannot be deleted.")
                        .build());
        repository.delete(object);
        log.debug("Device with ID: {} deleted successfully", id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeviceListDTO> getAll(DeviceFilterDTO filter) {
        log.debug("Fetching all devices with pagination: {}", filter);
        final Page<DeviceListDTO> page = customRepository.findByFilter(filter);
        log.debug("Devices fetched: {}", page.getContent());
        return page;
    }

    /**
     * Retrieves a device by its ID.
     * <p>
     * This method is transactional and will rollback if an exception occurs.
     * <p>
     * The device will be fetched from the database using the given ID.
     * If the device is not found, a ResourceNotFoundException will be thrown.
     * <p>
     * 
     * @param id the ID of the device to fetch
     * @return the fetched device
     * @throws ResourceNotFoundException if the device is not found
     */
    @Transactional(readOnly = true)
    public Device getById(Long id) {
        log.debug("Fetching device with ID: {}", id);
        final Device object = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + id));
        log.debug("Device fetched: {}", object);
        return object;
    }

    /**
     * Validates a device before update.
     * <p>
     * This method checks if the device is in use and if the conditions
     * that cannot be changed are equal between the entity and the DTO.
     * If the conditions are not met, a ValidException is thrown.
     * <p>
     * This method is used to ensure that the device name and brand cannot be
     * changed while the device is in use.
     * <p>
     * 
     * @param entity the device entity to validate
     * @param dto    the device DTO object containing the device data to validate
     * @throws ValidException if the device is in use and the conditions cannot be
     *                        changed
     */
    public void validateBeforeUpdate(Device entity, DeviceDTO dto) {
        log.debug("Validating device before update: {}", entity);
        final boolean isDeviceInUse = DeviceStateEnum.IN_USE.equals(entity.getState());
        log.debug("Is device in use: {}", isDeviceInUse);
        if (isDeviceInUse) {
            ExceptionsUtil.throwValidExceptions(
                    ExceptionUtilDTO.builder()
                            .condition(entity.getName().equals(dto.getName()))
                            .message("Device name cannot be changed while in use.")
                            .build(),
                    ExceptionUtilDTO.builder()
                            .condition(entity.getBrand().equals(dto.getBrand()))
                            .message("Device brand cannot be changed while in use.")
                            .build());
        }
    }
}
