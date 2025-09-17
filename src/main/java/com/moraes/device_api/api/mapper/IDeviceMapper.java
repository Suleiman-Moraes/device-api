package com.moraes.device_api.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;

@Mapper(componentModel = "spring")
public interface IDeviceMapper {

    /**
     * Maps a DeviceDTO object to a Device object.
     * <p>
     * Ignores unmapped target properties.
     * <p>
     * 
     * @param deviceDto the DeviceDTO object to map.
     * @return the mapped Device object.
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Device toEntity(DeviceDTO deviceDto);

    /**
     * Maps a Device object to a DeviceListDTO object.
     * <p>
     * Ignores unmapped target properties.
     * <p>
     * 
     * @param entity the Device object to map.
     * @return the mapped DeviceListDTO object.
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    DeviceListDTO toListDTO(Device entity);
}
