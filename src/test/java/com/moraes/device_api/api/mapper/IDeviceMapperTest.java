package com.moraes.device_api.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.device_api.api.model.Device;
import com.moraes.device_api.api.model.dto.device.DeviceDTO;
import com.moraes.device_api.api.model.dto.device.DeviceListDTO;
import com.moraes.device_api.api.model.enums.DeviceStateEnum;
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
        final Device entity = null;
        final DeviceListDTO dto = mapper.toListDTO(entity);
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

    @Test
    @DisplayName("JUnit test given null DeviceDTO when updateFromDeviceDTO then do nothing")
    void testGivenNullDeviceDTOWhenUpdateFromDeviceDTOThenDoNothing() {
        Device entity = mockDevice.mockEntity(1);
        final String nameBefore = entity.getName();
        final String brandBefore = entity.getBrand();
        final var stateBefore = entity.getState();
        final Long idBefore = entity.getId();
        final var creationTimeBefore = entity.getCreationTime();
        mapper.updateFromDeviceDTO(null, entity);

        assertNotNull(entity);
        assertEquals(nameBefore, entity.getName(), "Name should be equal");
        assertEquals(brandBefore, entity.getBrand(), "Brand should be equal");
        assertEquals(stateBefore, entity.getState(), "State should be equal");
        assertEquals(idBefore, entity.getId(), "ID should be equal");
        assertEquals(creationTimeBefore, entity.getCreationTime(), "Creation time should be equal");
    }

    @Test
    @DisplayName("JUnit test given DeviceDTO when updateFromDeviceDTO then update Device")
    void testGivenDeviceDTOWhenUpdateFromDeviceDTOThenUpdateDevice() {
        Device entity = mockDevice.mockEntity(1);
        final Long idBefore = entity.getId();
        final var creationTimeBefore = entity.getCreationTime();
        final DeviceDTO dto = mockDeviceDto.mockEntity(2);
        mapper.updateFromDeviceDTO(dto, entity);

        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName(), "Name should be equal");
        assertEquals(dto.getBrand(), entity.getBrand(), "Brand should be equal");
        assertEquals(dto.getState(), entity.getState(), "State should be equal");
        assertEquals(idBefore, entity.getId(), "ID should be equal");
        assertEquals(creationTimeBefore, entity.getCreationTime(), "Creation time should be equal");
    }

    @ParameterizedTest(name = "{index} => initial={0}, dto={1}, expectedBrand={2}, expectedName={3}, expectedState={4}, description={5}")
    @MethodSource("provideParametersForUpdatePartialFromDeviceDTO")
    @DisplayName("JUnit test given DeviceDTO when updatePartialFromDeviceDTO then only non-null fields are updated")
    void testGivenDeviceDTOWhenUpdatePartialFromDeviceDTOThenOnlyNonNullFieldsAreUpdated(Device initial,
            DeviceDTO dto, String expectedBrand, String expectedName, DeviceStateEnum expectedState,
            String description) {

        final Device entity = Device.builder()
                .brand(initial.getBrand())
                .name(initial.getName())
                .state(initial.getState())
                .build();

        assertNotNull(entity, "Entity should not be null before update");

        mapper.updatePartialFromDeviceDTO(dto, entity);

        assertEquals(expectedBrand, entity.getBrand(), "Brand should be equal " + expectedBrand);
        assertEquals(expectedName, entity.getName(), "Name should be equal " + expectedName);
        assertEquals(expectedState, entity.getState(), "State should be equal " + expectedState);
    }

    @Test
    @DisplayName("JUnit test given null value when toListDTOs then return null")
    void testGivenNullValueWhenToListDTOsThenReturnNull() {
        final List<Device> entities = null;
        final var dtos = mapper.toListDTOs(entities);
        assertNull(dtos, "DTOs should be null");
    }

    @Test
    @DisplayName("JUnit test given list of Device when toListDTOs then return list of DeviceListDTO")
    void testGivenListOfDeviceWhenToListDTOsThenReturnListOfDeviceListDTO() {
        final List<Device> entities = List.of(mockDevice.mockEntity(1), mockDevice.mockEntity(2));
        final var dtos = mapper.toListDTOs(entities);

        assertNotNull(dtos, "DTOs should not be null");
        assertEquals(2, dtos.size(), "DTOs size should be 2");

        for (int i = 0; i < entities.size(); i++) {
            final var entity = entities.get(i);
            final var dto = dtos.get(i);

            assertEquals(entity.getId(), dto.getId(), "ID should be equal");
            assertEquals(entity.getName(), dto.getName(), "Name should be equal");
            assertEquals(entity.getBrand(), dto.getBrand(), "Brand should be equal");
            assertEquals(entity.getState(), dto.getState(), "State should be equal");
            assertEquals(entity.getCreationTime(), dto.getCreationTime(), "Creation time should be equal");
        }
    }

    private static Stream<Arguments> provideParametersForUpdatePartialFromDeviceDTO() {
        final DeviceStateEnum available = DeviceStateEnum.AVAILABLE;
        final DeviceStateEnum inUse = DeviceStateEnum.IN_USE;

        final Device initial = Device.builder()
                .brand("OldBrand")
                .name("OldName")
                .state(available)
                .build();

        final DeviceDTO dtoAll = DeviceDTO.builder()
                .brand("NewBrand")
                .name("NewName")
                .state(inUse)
                .build();

        final DeviceDTO dtoBrandOnly = DeviceDTO.builder()
                .brand("BrandOnly")
                .build();

        final DeviceDTO dtoNameOnly = DeviceDTO.builder()
                .name("NameOnly")
                .build();

        final DeviceDTO dtoStateOnly = DeviceDTO.builder()
                .state(inUse)
                .build();

        return Stream.of(
                Arguments.of(initial, null, "OldBrand", "OldName", available, "DTO is null -> no changes"),
                Arguments.of(initial, dtoAll, "NewBrand", "NewName", inUse, "All fields present -> all updated"),
                Arguments.of(initial, dtoBrandOnly, "BrandOnly", "OldName", available,
                        "Only brand present -> only brand updated"),
                Arguments.of(initial, dtoNameOnly, "OldBrand", "NameOnly", available,
                        "Only name present -> only name updated"),
                Arguments.of(initial, dtoStateOnly, "OldBrand", "OldName", inUse,
                        "Only state present -> only state updated"));
    }
}
