package com.moraes.device_api.api.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
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
    @Named("toListDTOs")
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    DeviceListDTO toListDTO(Device entity);

    @IterableMapping(qualifiedByName = "toListDTOs")
    List<DeviceListDTO> toListDTOs(List<Device> entities);

    /**
     * Updates a Device object with the values from a DeviceDTO object.
     * <p>
     * Ignores unmapped target properties.
     * <p>
     * 
     * @param dto    the DeviceDTO object with the values to update.
     * @param entity the Device object to update.
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromDeviceDTO(DeviceDTO dto, @MappingTarget Device entity);

    /**
     * Updates a Device object with the values from a DeviceDTO object,
     * ignoring null values and unmapped target properties.
     * <p>
     * 
     * @param dto    the DeviceDTO object with the values to update.
     * @param entity the Device object to update.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updatePartialFromDeviceDTO(DeviceDTO dto, @MappingTarget Device entity);
}
