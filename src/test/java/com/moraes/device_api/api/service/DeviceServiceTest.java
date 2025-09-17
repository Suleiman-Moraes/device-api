package com.moraes.device_api.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.device_api.api.exception.ResourceNotFoundException;
import com.moraes.device_api.api.mapper.IDeviceMapper;
import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.repository.IDeviceRepository;
import com.moraes.device_api.mock.MockDevice;
import com.moraes.device_api.mock.MockDeviceDTO;
import com.moraes.device_api.mock.MockDeviceListDTO;

class DeviceServiceTest {

    @Spy
    @InjectMocks
    private DeviceService service;

    @Mock
    private IDeviceRepository repository;

    @Mock
    private IDeviceMapper mapper;

    private MockDevice input;
    private MockDeviceDTO mockDeviceDTO;
    private MockDeviceListDTO mockDeviceListDTO;

    private final Long id = 1l;
    private Device entity;

    @BeforeEach
    void setUp() {
        input = new MockDevice();
        mockDeviceDTO = new MockDeviceDTO();
        mockDeviceListDTO = new MockDeviceListDTO();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setId(id);
    }

    @Test
    @DisplayName("JUnit test given DeviceDTO when insert then return Device ID")
    void testGivenDeviceDTOWhenToEntityThenParseToDevice() {
        final var dto = mockDeviceDTO.mockEntity(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        final Long response = service.insert(dto);

        assertNotNull(response, "ID should not be null");
        assertEquals(id, response, "ID should be equal " + id);
    }

    @Test
    @DisplayName("JUnit test given Device ID when getById then return Device")
    void testGivenDeviceIDWhenGetByIdThenReturnDevice() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        final Device response = service.getById(id);

        assertNotNull(response, "Device should not be null");
        assertEquals(id, response.getId(), "ID should be equal " + id);
        assertEquals(entity.getName(), response.getName(), "Name should be equal " + entity.getName());
        assertEquals(entity.getBrand(), response.getBrand(), "Brand should be equal " + entity.getBrand());
        assertEquals(entity.getState(), response.getState(), "State should be equal " + entity.getState());
        assertEquals(entity.getCreationTime(), response.getCreationTime(),
                "Creation Time should be equal " + entity.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test given Device ID invalid when getById then throw ResourceNotFoundException")
    void testGivenDeviceIDInvalidWhenGetByIdThenThrowResourceNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(id);
        });
        assertEquals("Device not found with ID: " + id, ex.getMessage(), "Message should be equal");
    }

    @Test
    @DisplayName("JUnit test given Device ID when getDTOById then return DeviceDTO")
    void testGivenDeviceIDWhenGetDTOByIdThenReturnDeviceDTO() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toListDTO(entity)).thenReturn(mockDeviceListDTO.mockEntity(1));

        final DeviceListDTO response = service.getDTOById(id);

        assertNotNull(response, "DeviceDTO should not be null");
        assertEquals(entity.getName(), response.getName(), "Name should be equal " + entity.getName());
        assertEquals(entity.getBrand(), response.getBrand(), "Brand should be equal " + entity.getBrand());
        assertEquals(entity.getState(), response.getState(), "State should be equal " + entity.getState());
    }
}
