package com.moraes.device_api.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.device_api.api.model.Device;

public interface IDeviceRepository extends JpaRepository<Device, Long> {

}
