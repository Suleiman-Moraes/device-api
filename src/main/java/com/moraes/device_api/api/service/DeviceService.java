package com.moraes.device_api.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.device_api.api.mapper.IDeviceMapper;
import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
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
}
