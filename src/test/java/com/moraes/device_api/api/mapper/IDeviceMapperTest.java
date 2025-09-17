package com.moraes.device_api.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.mock.MockDevice;
import com.moraes.device_api.mock.MockDeviceDTO;

class IDeviceMapperTest {

    @Spy
	@InjectMocks
	private IDeviceMapper mapper = Mappers.getMapper(IDeviceMapper.class);

    private MockDevice mockDevice;
    private MockDeviceDTO mockDeviceDto;
    
    @BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockDevice = new MockDevice();
		mockDeviceDto = new MockDeviceDTO();
	}

    @Test
	@DisplayName("JUnit test given null value when toEntity then return null")
    void testGivenNullValueWhenToEntityThenReturnNull() {
        final Device entity = mapper.toEntity(null);
        assertNull(entity, "Entity should be null");
    }

    @Test
	@DisplayName("JUnit test given DeviceDTO when toEntity then parse to Device")
    void testGivenDeviceDTOWhenToEntityThenParseToDevice() {
        final DeviceDTO dto = mockDeviceDto.mockEntity(1);
        final Device entity = mapper.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName(), "Name should be equal");
        assertEquals(dto.getBrand(), entity.getBrand(), "Brand should be equal");
        assertEquals(dto.getState(), entity.getState(), "State should be equal");
        assertNull(entity.getId(), "ID should be null");
        assertNull(entity.getCreationTime(), "Creation time should be null");
    }

    @Test
	@DisplayName("JUnit test given null value when toListDTO then return null")
    void testGivenNullValueWhenToListDTOThenReturnNull() {
        final DeviceListDTO dto = mapper.toListDTO(null);
        assertNull(dto, "DTO should be null");
    }

    @Test
    @DisplayName("JUnit test given Device when toListDTO then parse to DeviceListDTO")
    void testGivenDeviceWhenToListDTOThenParseToDeviceListDTO() {
        final Device entity = mockDevice.mockEntity(1);
        final DeviceListDTO dto = mapper.toListDTO(entity);
        
        assertNotNull(dto);
        assertEquals(entity.getName(), dto.getName(), "Name should be equal");
        assertEquals(entity.getBrand(), dto.getBrand(), "Brand should be equal");
        assertEquals(entity.getState(), dto.getState(), "State should be equal");
        assertEquals(entity.getId(), dto.getId(), "ID should be equal");
        assertEquals(entity.getCreationTime(), dto.getCreationTime(), "Creation time should be equal");
    }
}
