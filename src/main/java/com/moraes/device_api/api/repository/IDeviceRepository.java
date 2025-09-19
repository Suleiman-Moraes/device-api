package com.moraes.device_api.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;


public interface IDeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByState(DeviceStateEnum state);

    List<Device> findByBrand(String brand);
}
