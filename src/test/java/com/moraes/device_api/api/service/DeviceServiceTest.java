package com.moraes.device_api.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.device_api.api.mapper.IDeviceMapper;
import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.repository.IDeviceRepository;
import com.moraes.device_api.mock.MockDevice;
import com.moraes.device_api.mock.MockDeviceDTO;

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

    private final Long id = 1l;
    private Device entity;

    @BeforeEach
    void setUp() {
        input = new MockDevice();
        mockDeviceDTO = new MockDeviceDTO();
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
}
