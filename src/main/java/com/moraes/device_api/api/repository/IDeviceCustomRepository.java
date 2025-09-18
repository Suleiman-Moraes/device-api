package com.moraes.device_api.api.repository;

import org.springframework.data.domain.Page;

import com.moraes.device_api.api.model.dto.device.DeviceFilterDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;

public interface IDeviceCustomRepository {

    /**
     * Returns a page of DeviceListDTO given a filter.
     *
     * If the filter's property is empty, it will be set to "id" and the direction
     * will be set to DESC.
     *
     * If the filter's pagination is enabled, the total count of devices will be
     * returned.
     * If the filter's pagination is disabled, the total count of devices in the
     * list will be returned.
     *
     * @param filter the filter to apply
     * @return a page of DeviceListDTO
     * @throws Exception if an error occurs while executing the query
     */
    Page<DeviceListDTO> findByFilter(DeviceFilterDTO filter);
}
