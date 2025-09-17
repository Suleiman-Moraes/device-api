package com.moraes.device_api.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.device_api.api.exception.ResourceNotFoundException;
import com.moraes.device_api.api.mapper.IDeviceMapper;
import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.repository.IDeviceRepository;
import com.moraes.device_api.api.service.interfaces.IDeviceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceService implements IDeviceService {

    private final IDeviceRepository repository;

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
}
