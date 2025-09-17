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
import com.moraes.device_api.mock.MockDeviceDTO;

class IDeviceMapperTest {

    @Spy
	@InjectMocks
	private IDeviceMapper mapper = Mappers.getMapper(IDeviceMapper.class);

    private MockDeviceDTO mockDeviceDto;
    
    @BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockDeviceDto = new MockDeviceDTO();
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
}
